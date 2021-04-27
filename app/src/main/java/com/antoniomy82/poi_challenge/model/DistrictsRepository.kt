package com.antoniomy82.poi_challenge.model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.antoniomy82.poi_challenge.ui.homedistrict.HomeDistrictFragment
import com.antoniomy82.poi_challenge.utils.PoisUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DistrictsRepository {

    fun getDistrict(context:Context): MutableLiveData<District>? {
        val retrieveDistrict=MutableLiveData<District>()

        val call: Call<District>? = ApiAdapter().api?.getDistrict()


        return call?.let { mCallback(it,retrieveDistrict, context) }
    }

    fun getDistrict1(context:Context): MutableLiveData<District>? {
        val retrieveDistrict=MutableLiveData<District>()

        val call: Call<District>? = ApiAdapter().api?.getDistrict1()


        return call?.let { mCallback(it,retrieveDistrict, context) }
    }

    fun getDistrict2(context:Context): MutableLiveData<District>? {
        val retrieveDistrict=MutableLiveData<District>()

        val call: Call<District>? = ApiAdapter().api?.getDistrict2()


        return call?.let { mCallback(it,retrieveDistrict, context) }
    }

    fun getDistrict3(context:Context): MutableLiveData<District>? {
        val retrieveDistrict=MutableLiveData<District>()

        val call: Call<District>? = ApiAdapter().api?.getDistrict3()


        return call?.let { mCallback(it,retrieveDistrict, context) }
    }

    fun getDistrict4(context:Context): MutableLiveData<District>? {
        val retrieveDistrict=MutableLiveData<District>()

        val call: Call<District>? = ApiAdapter().api?.getDistrict4()


        return call?.let { mCallback(it,retrieveDistrict, context) }
    }

    fun getDistrict5(context:Context): MutableLiveData<District>? {
        val retrieveDistrict=MutableLiveData<District>()

        val call: Call<District>? = ApiAdapter().api?.getDistrict5()


        return call?.let { mCallback(it,retrieveDistrict, context) }
    }

    fun getDistrict6(context:Context): MutableLiveData<District>? {
        val retrieveDistrict=MutableLiveData<District>()

        val call: Call<District>? = ApiAdapter().api?.getDistrict6()


        return call?.let { mCallback(it,retrieveDistrict, context) }
    }


    private fun mCallback(call: Call<District>, retrieveDistrict:MutableLiveData<District>, context:Context): MutableLiveData<District>{
        call.enqueue(object : Callback<District> {
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
                Toast.makeText(context, "ERROR API SERVICES", Toast.LENGTH_LONG).show()
                PoisUtils.replaceFragment(HomeDistrictFragment(),  (context as AppCompatActivity).supportFragmentManager)
            }

        })


        return retrieveDistrict
    }
}