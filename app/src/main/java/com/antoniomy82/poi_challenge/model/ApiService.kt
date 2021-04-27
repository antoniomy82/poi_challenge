package com.antoniomy82.poi_challenge.model

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("1" )
    fun getDistrict(): Call<District>

    @GET("3" )
    fun getDistrict1(): Call<District>

    @GET("7" )
    fun getDistrict2(): Call<District>

    @GET("11" )
    fun getDistrict3(): Call<District>

    @GET("14" )
    fun getDistrict4(): Call<District>

    @GET("15" )
    fun getDistrict5(): Call<District>

    @GET("5" )
    fun getDistrict6(): Call<District>
}