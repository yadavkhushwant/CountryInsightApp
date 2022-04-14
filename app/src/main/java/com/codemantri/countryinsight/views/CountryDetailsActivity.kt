package com.codemantri.countryinsight.views

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.codemantri.countryinsight.R
import com.codemantri.countryinsight.data.model.country.CountryDataItem
import com.codemantri.countryinsight.databinding.ActivityCountryDetailsBinding
import com.codemantri.countryinsight.utils.LoadingDialog
import com.codemantri.countryinsight.utils.Status.ERROR
import com.codemantri.countryinsight.utils.Status.SUCCESS
import com.codemantri.countryinsight.utils.Status.LOADING
import com.codemantri.countryinsight.viewmodel.CountryDetailsViewModel
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import java.lang.NullPointerException
import kotlin.text.StringBuilder

@AndroidEntryPoint
class CountryDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCountryDetailsBinding
    private val viewModel: CountryDetailsViewModel by viewModels()
    lateinit var countryName: String
    lateinit var countryDetails: CountryDataItem
    lateinit var loadingDialog: AlertDialog
    private val sbBorderCountries = StringBuilder()

    companion object{
        const val TAG = "CountryDetailsActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        // Get country name from dashboard activity
        countryName = intent.extras?.getString("countryName")!!

        getBorderCountries()

        //Initialize Loading Dialog
        loadingDialog = LoadingDialog.createDialog(this, false).create()

        getCountryData(countryName)


    }

    private fun getCountryData(countryName: String) {
        // Call API
        viewModel.getCountryData(countryName)

        //Observe Live Data
        viewModel.countryData.observe(this) {
            it.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                        resource.data.let { countryData ->
                            countryDetails = countryData!![0]
                            setupViews()
                            loadMap()

                            try {
                                for (i in countryDetails.borders) {
                                    viewModel.getBorderCountries(i)
                                }
                            } catch (npe: NullPointerException) {
                                npe.printStackTrace()
                            }

                            loadingDialog.dismiss()
                        }
                    }
                    ERROR -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        loadingDialog.dismiss()
                    }
                    LOADING -> {
                        loadingDialog.show()
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews(){

        Glide.with(this).load(countryDetails.flags.png).into(binding.ivCountryFlag)
        Glide.with(this)
            .load(countryDetails.coatOfArms.png)
            .placeholder(R.drawable.img_no_image)
            .into(binding.ivCoatOfArms)

        binding.countryDatailsToolbar.title = countryDetails.name.common

        binding.tvOfficialName.text = countryDetails.name.official
        binding.tvCapital.text = countryDetails.capital[0]
        binding.tvDemonyms.text = countryDetails.demonyms.eng.m
        binding.tvPopulation.text = "${countryDetails.population} Peoples"
        binding.tvArea.text = "${countryDetails.area} km²"

        try{
            binding.tvCurrency.text = getCurrency()
            binding.tvLanguages.text = getLanguages()
        } catch(e: Exception){
            e.printStackTrace()
        }

        binding.tvContinent.text = countryDetails.continents?.get(0)
        binding.tvRegion.text = countryDetails.region
        binding.tvSubRegion.text = countryDetails.subregion

        val sbTimeZones = StringBuilder()
        for (i in countryDetails.timezones){
            sbTimeZones.append("$i , ")

        }
        sbTimeZones.deleteCharAt(sbTimeZones.length-2)
        binding.tvTimezones.text = sbTimeZones. toString()

        binding.tvLatlng.text = "${countryDetails.latlng[0]} | ${countryDetails.latlng[1]}"

        if (countryDetails.independent){
            binding.ivIndependenceStatus.setImageResource(R.drawable.ic_true)
        }
        if (countryDetails.unMember){
            binding.ivUnMemberStatus.setImageResource(R.drawable.ic_true)
        }
        if (countryDetails.landlocked){
            binding.tvLandlockedStatus.text = "Yes"
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadMap(){
        binding.webViewMap.webViewClient = WebViewClient()
        binding.webViewMap.settings.javaScriptEnabled = true
        binding.webViewMap.loadUrl(countryDetails.maps.googleMaps)
    }

    private fun getBorderCountries(){
        viewModel.borderCountryName.observe(this) { name ->
            sbBorderCountries.append("${name.common} , ")
            try {
                if (sbBorderCountries.toString().isEmpty()) {
                    binding.tvBorderCountries.text = sbBorderCountries.toString()
                } else binding.tvBorderCountries.text = "none"

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getCurrency(): String {
        val currencyKey: MutableSet<String> = countryDetails.currencies.keySet()
        Log.d(TAG, currencyKey.toString())
        val currencyJson = countryDetails.currencies[currencyKey.elementAt(0)].asJsonObject
        Log.d(TAG, currencyJson.toString())
        Log.d(TAG, currencyJson["name"].asString)
        return currencyJson["name"].asString
    }

    private fun getLanguages(): String{
        val languagesJson: JsonObject = countryDetails.languages
        val keys: MutableSet<String> = languagesJson.keySet()
        val sbLanguages = StringBuilder()
        keys.forEach {
            val languageName = languagesJson[it].asString
            sbLanguages.append("$languageName , ")
        }

        sbLanguages.deleteCharAt(sbLanguages.length-2)
        return sbLanguages.toString()
    }

    private fun setupToolbar(){
        setSupportActionBar(binding.countryDatailsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
    }

}