package com.antoniomy82.poi_challenge.model

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DistrictsRepository {

    fun getDistrict1(){
        val retrieveList = ArrayList<District>()

        ApiAdapter().api?.getDistrict1()?.enqueue(object : Callback<District>{

            override fun onResponse(call: Call<District>, response: Response<District>) {
                Log.d("__response", response.body().toString())
            }

            override fun onFailure(call: Call<District>, t: Throwable) {
                Log.e("__error", t.toString())
            }

        })
    }
}