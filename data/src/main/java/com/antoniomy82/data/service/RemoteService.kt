package com.antoniomy82.data.service

import com.antoniomy82.data.model.District
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface RemoteService {
    @GET fun getDistrictList(@Url mUrl: String): Call<District>
}