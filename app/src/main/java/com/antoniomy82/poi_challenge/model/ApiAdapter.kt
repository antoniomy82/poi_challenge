package com.antoniomy82.poi_challenge.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiAdapter {
    private val urlCities = "https://cityme-services.prepro.site/app_dev.php/api/districts/"
    var api: ApiService? = null

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(urlCities)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create<ApiService>(ApiService::class.java)
    }
}