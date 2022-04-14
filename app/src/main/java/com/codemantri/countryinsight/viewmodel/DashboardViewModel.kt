package com.codemantri.countryinsight.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codemantri.countryinsight.data.model.country.CountryData
import com.codemantri.countryinsight.data.repository.CountryRepository
import com.codemantri.countryinsight.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val repository: CountryRepository) :
    ViewModel() {

    private val _allCountryData: MutableLiveData<Resource<CountryData>> = MutableLiveData()

    val allCountryData: LiveData<Resource<CountryData>>
        get() = _allCountryData

    init {
        getCountryList()
    }

    fun getCountryList() {
        viewModelScope.launch(Dispatchers.IO) {
            _allCountryData.postValue(Resource.loading(null))
            try {
                val response = repository.getAllCountries()
                if (response.isSuccessful)
                    _allCountryData.postValue(Resource.success(response.body()))
            } catch (e: Exception) {
                _allCountryData.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }
}

















