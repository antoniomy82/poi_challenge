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
import com.antoniomy82.poi_challenge.model.District
import com.antoniomy82.poi_challenge.ui.PoisDistrictListAdapter
import java.lang.ref.WeakReference
import java.util.*

class PoisViewModel : ViewModel() {

    //Fragment values
    var frgActivity: WeakReference<Activity>? = null
    var frgContext: WeakReference<Context>? = null
    var frgView: WeakReference<View>? = null

    //Main fragment values
    val districtTittle= MutableLiveData<String>()
    val poisCount = MutableLiveData<String>().also { it.value = "0" }


    fun setUI(){
       // var header:HeaderBinding
         //header.headerVM=this
        val headerTitle=frgView?.get()?.findViewById<View>(R.id.headerTitle) as TextView
        headerTitle.text="MADRID"

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
            PoisDistrictListAdapter(this, mDistrict,
                it
            )
        }
    }

    fun setTittleFromAdapter(tittle:String, count:String ){
        districtTittle.value = tittle.toUpperCase(Locale.ROOT)
        poisCount.value=count

        Log.d("tittleBar", tittle +"count:"+count)
    }
}