package com.codemantri.countryinsight.data.model.country

import com.google.gson.JsonObject


data class CountryDataItem(
    val altSpellings: List<String>, //OK
    val car: Car, //OK
    val cioc: String, //OK


    val fifa: String,//OK
    val gini: Gini, //GENERIC
    val languages: JsonObject, //Generic
    val postalCode: PostalCode,//ok
    val startOfWeek: String, //OK
    val status: String, //OK
    val tld: List<String>, //OK
    val translations: Translations, //OK But can be made generic

    val currencies: JsonObject, //Generic
    val borders: List<String>,
    val demonyms: Demonyms, //OK
    val name: Name, //OK but Native Not ok
    val area: Double, //OK                   //COMP
    val capital: List<String>, //OK         //COMP
    val coatOfArms: CoatOfArms, //OK
    val continents: List<String>?, //OK
    val flag: String, //OK
    val flags: Flags, //OK
    val independent: Boolean, //OK
    val landlocked: Boolean, //OK
    val latlng: List<Double>, //OK (Geo)
    val maps: Maps,//OK (Geo)
    val population: Int, //OK
    val region: String, //OK (Geo)
    val subregion: String, //OK (Geo)
    val timezones: List<String>,//OK (Geo)
    val unMember: Boolean //OK
)