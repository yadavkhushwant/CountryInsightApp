package com.codemantri.countryinsight.views

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.codemantri.countryinsight.R
import com.codemantri.countryinsight.databinding.ActivityDashboardBinding
import com.codemantri.countryinsight.utils.LoadingDialog
import com.codemantri.countryinsight.utils.Status.ERROR
import com.codemantri.countryinsight.utils.Status.SUCCESS
import com.codemantri.countryinsight.utils.Status.LOADING
import com.codemantri.countryinsight.viewmodel.DashboardViewModel
import com.google.android.material.snackbar.Snackbar

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()
    lateinit var dialog: Dialog
    lateinit var adapter: ArrayAdapter<String>
    lateinit var loadingDialog: AlertDialog
    private var countryList: List<String> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getCountryList()
        initializeDialog()

        binding.tvSelectCountry.setOnClickListener {
            if (countryList.isNotEmpty())
                selectCountry()
            else Snackbar.make(binding.root, "Unable to load countries list", Snackbar.LENGTH_INDEFINITE).setAction("Retry"){
                viewModel.getCountryList()
            }.show()
        }

        binding.btnShowDetails.setOnClickListener {
            if (binding.tvSelectCountry.text.isNotEmpty()) {
                val intent = Intent(this, CountryDetailsActivity::class.java)
                intent.putExtra("countryName", binding.tvSelectCountry.text.toString().trim())
                startActivity(intent)
            } else
                Toast.makeText(this, "Please select country", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializeDialog() {
        dialog = Dialog(this)

        // Set layout to dialog
        dialog.setContentView(R.layout.dialog_spinner)

        // Set height and width of dialog
        dialog.window?.setLayout(650, 800)

        // Set transparent background
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun getCountryList(){
        //Initialize Loading Dialog
        loadingDialog = LoadingDialog.createDialog(this, false).create()
        // Observe live data
        viewModel.allCountryData.observe(this) {
            it.let { resource ->
                when (resource.status) {

                    SUCCESS -> {
                        resource.data.let { countryData ->
                            countryList =
                                countryData?.map { countryDataItem -> countryDataItem.name.common }
                                    ?.toList()!!

                            adapter = ArrayAdapter<String>(this,
                                R.layout.support_simple_spinner_dropdown_item,
                                countryList)
                            loadingDialog.dismiss()
                        }
                    }
                    ERROR -> {
                        loadingDialog.dismiss()
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                    LOADING -> {
                        loadingDialog.show()
                    }
                }
            }
        }
    }

    private fun selectCountry() {
        // Show dialog
        dialog.show()

        // Find and initialize views
        val searchCountry = dialog.findViewById<EditText>(R.id.et_search_country)
        val countryListView = dialog.findViewById<ListView>(R.id.listview_country_list)


        // Set adapter
        countryListView.adapter = adapter

        // Search and filter listview
        searchCountry.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.filter.filter(p0)
            }

            override fun afterTextChanged(p0: Editable?) {}

        })


        countryListView.onItemClickListener = this
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedCountry = parent?.getItemAtPosition(position) as String
        binding.tvSelectCountry.text = selectedCountry
        dialog.dismiss()
    }
}