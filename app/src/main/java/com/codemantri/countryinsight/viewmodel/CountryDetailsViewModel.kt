package com.codemantri.countryinsight.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codemantri.countryinsight.data.model.country.CountryData
import com.codemantri.countryinsight.data.model.country.Name
import com.codemantri.countryinsight.data.repository.CountryRepository
import com.codemantri.countryinsight.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CountryDetailsViewModel @Inject constructor(private val repository: CountryRepository): ViewModel() {
    private val _countryData:MutableLiveData<Resource<CountryData>> = MutableLiveData()
    private val _borderCountryName: MutableLiveData<Name> = MutableLiveData()

    val countryData: LiveData<Resource<CountryData>>
    get() = _countryData

    val borderCountryName: LiveData<Name>
    get() = _borderCountryName

    fun getCountryData(countryName: String){
        viewModelScope.launch(Dispatchers.IO){
          _countryData.postValue(Resource.loading(null))
            try {
                val response = repository.getCountryData(countryName)
                if (response.isSuccessful)
                    _countryData.postValue(Resource.success(response.body()))
            } catch (e: Exception){
                _countryData.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }


    fun getBorderCountries(countryCode: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = repository.getBorderCountries(countryCode)
                if (response.isSuccessful)
                    _borderCountryName.postValue(response.body()?.get(0)?.name)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

}