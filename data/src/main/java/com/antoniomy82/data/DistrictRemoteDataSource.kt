package com.antoniomy82.data

import android.util.Log
import com.antoniomy82.data.model.District
import com.antoniomy82.data.service.RemoteConnection
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DistrictRemoteDataSource {

    fun getDistrictList(urlId: String): MutableStateFlow<District> =
        requestDistrictList(RemoteConnection.remoteService.getDistrictList(urlCities + urlId))


    private fun requestDistrictList(call: Call<District>): MutableStateFlow<District> {
        val retrieveDistrict = MutableStateFlow(District())

        call.enqueue(object : Callback<District> {
            override fun onResponse(call: Call<District>, response: Response<District>) {
                Log.d("_response", response.body().toString())
                response.body()?.let { retrieveDistrict.value = it }
            }

            override fun onFailure(call: Call<District>, t: Throwable) {
                Log.e("__error", t.toString())
            }

        })
        return retrieveDistrict
    }
}