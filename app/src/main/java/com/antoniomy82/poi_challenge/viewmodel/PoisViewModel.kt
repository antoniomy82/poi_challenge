package com.antoniomy82.poi_challenge.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy82.poi_challenge.R
import com.antoniomy82.poi_challenge.databinding.PopUpPoisDetailBinding
import com.antoniomy82.poi_challenge.model.District
import com.antoniomy82.poi_challenge.model.Pois
import com.antoniomy82.poi_challenge.ui.PoisDistrictListAdapter
import com.antoniomy82.poi_challenge.utils.PopupUtil
import com.bumptech.glide.Glide
import java.lang.ref.WeakReference
import java.util.*

class PoisViewModel : ViewModel() {

    //Fragment values
    var frgActivity: WeakReference<Activity>? = null
    var frgContext: WeakReference<Context>? = null
    var frgView: WeakReference<View>? = null

    //Main fragment values
    val districtTittle = MutableLiveData<String>()
    val poisCount = MutableLiveData<String>().also { it.value = "0" }



    fun setUI() {

        val headerTitle = frgView?.get()?.findViewById<View>(R.id.headerTitle) as TextView
        headerTitle.text = "MADRID"

    }

    fun setFragmentBinding(
        frgActivity: Activity,
        frgContext: Context,
        frgView: View
    ) {
        this.frgActivity = WeakReference(frgActivity)
        this.frgContext = WeakReference(frgContext)
        this.frgView = WeakReference(frgView)
    }

    //Set RecyclerView
    fun setRecyclerViewAdapter(mDistrict: District) {

        val mRecycler: RecyclerView =
            frgView?.get()?.findViewById(R.id.rvPois) as RecyclerView
        val recyclerView: RecyclerView = mRecycler
        val manager: RecyclerView.LayoutManager =
            LinearLayoutManager(frgActivity?.get()) //Orientation
        recyclerView.layoutManager = manager
        recyclerView.adapter = frgContext?.get()?.let {
            PoisDistrictListAdapter(
                this, mDistrict,
                it
            )
        }
    }

    fun setTittleFromAdapter(tittle: String, count: String) {
        districtTittle.value = tittle.toUpperCase(Locale.ROOT)
        poisCount.value = count

        Log.d("tittleBar", tittle + "count:" + count)
    }

    fun popUpDetail(mPoi: Pois) {

        val popUpBinding = frgActivity?.get()?.let {
            frgContext?.get()?.let { it1 ->
                PopupUtil.genericDialog(
                    it,
                    it1, R.layout.pop_up_pois_detail
                ) as PopUpPoisDetailBinding
            }
        }


        popUpBinding?.vm = this
        popUpBinding?.titlePopup?.text = mPoi.name
        popUpBinding?.streetPopup?.text = mPoi.description

        //Set image
        if (mPoi.image?.url != null) {
            Log.d("galleryImages",mPoi.galleryImages?.get(0)?.url.toString())
            frgContext?.get()?.let {
                popUpBinding?.photoPopup?.let { it1 -> Glide.with(it).load(mPoi.image?.url).into(it1) }
            }
        }

        //Set icon image
        if (mPoi.category?.icon?.url != null) {
            frgContext?.get()?.let {
                popUpBinding?.iconPopup?.let { it1 -> Glide.with(it).load(mPoi.category?.icon?.url).into(it1) }
            }
        }

        //Set likes counter
        if( mPoi.likesCount==null) popUpBinding?.likeQty?.text = "0"
        else popUpBinding?.likeQty?.text  = mPoi.likesCount.toString()


    }
}