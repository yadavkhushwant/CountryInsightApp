package com.codemantri.countryinsight.data.remote

import com.codemantri.countryinsight.data.model.country.CountryData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {

    @GET("name/{countryname}/")
    suspend fun getCountryData(
        @Path("countryname") countryname: String, @Query("fullText") fullText:Boolean = true): Response<CountryData>


    @GET("all")
    suspend fun getAllCountries(): Response<CountryData>


    @GET("alpha/{countryCode}")
    suspend fun getBorderCountries(
        @Path("countryCode") countryCode: String
    ): Response<CountryData>

}