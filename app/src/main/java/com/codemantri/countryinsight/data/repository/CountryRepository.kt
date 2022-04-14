package com.codemantri.countryinsight.data.repository

import com.codemantri.countryinsight.data.remote.RetrofitService
import javax.inject.Inject


class CountryRepository @Inject constructor(private val retrofitService: RetrofitService) {


    suspend fun getAllCountries() = retrofitService.getAllCountries()

    suspend fun getCountryData(countryName: String) = retrofitService.getCountryData(countryName)

    suspend fun getBorderCountries(countryCode: String) = retrofitService.getBorderCountries(countryCode)

}