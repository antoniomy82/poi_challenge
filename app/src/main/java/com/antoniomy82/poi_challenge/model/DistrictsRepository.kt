package com.antoniomy82.poi_challenge.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DistrictsRepository {

    fun getDistrict1(): MutableLiveData<District> {
        val retrieveDistrict=MutableLiveData<District>()

        val call: Call<District>? = ApiAdapter().api?.getDistrict1()

        call?.enqueue(object : Callback<District> {
            override fun onResponse(call: Call<District>, response: Response<District>) {

            var mResponse: District ?=null

                if (response.isSuccessful) {

                    val responseObtained = response.body()

                    mResponse = District(
                        poisCount = responseObtained?.poisCount,
                        id = responseObtained?.id,
                        name = responseObtained?.name,
                        image = responseObtained?.image,
                        galleryImages = responseObtained?.galleryImages,
                        coordinates = responseObtained?.coordinates,
                        video = responseObtained?.video,
                        audio = responseObtained?.audio,
                        pois = responseObtained?.pois
                    )
                    // Log.d("__restrie",mResponse.toString())
                }

                retrieveDistrict.value=mResponse

            }

            override fun onFailure(call: Call<District>, t: Throwable) {
                Log.e("__error", t.toString())
            }

        })

        //Log.d("__retrieve", retrieveDistrict.toString())

        return retrieveDistrict
    }
}