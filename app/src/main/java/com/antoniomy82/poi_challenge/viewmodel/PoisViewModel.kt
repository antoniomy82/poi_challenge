package com.antoniomy82.poi_challenge.viewmodel

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy82.poi_challenge.R
import com.antoniomy82.poi_challenge.model.District
import com.antoniomy82.poi_challenge.ui.PoisDistrictListAdapter
import java.lang.ref.WeakReference

class PoisViewModel : ViewModel() {

    //Fragment values
    var frgActivity: WeakReference<Activity>? = null
    var frgContext: WeakReference<Context>? = null
    var frgView: WeakReference<View>? = null

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

}