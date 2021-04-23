package com.antoniomy82.poi_challenge.model

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("1")
    fun getDistrict1(): Call<District>
}