package com.example.locationtestdi.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiResturant {

    @GET("/rigo/vivaeltaco/locations.php")
    fun getDefinitions(): Call<List<JsonLocation.JsonLocationItem>>



}
