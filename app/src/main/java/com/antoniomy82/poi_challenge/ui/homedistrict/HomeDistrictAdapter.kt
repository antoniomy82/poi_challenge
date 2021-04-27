package com.antoniomy82.poi_challenge.ui.homedistrict

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy82.poi_challenge.R
import com.antoniomy82.poi_challenge.databinding.AdapterHomeDistrictsBinding
import com.antoniomy82.poi_challenge.model.DistrictListMockUp
import com.antoniomy82.poi_challenge.ui.districtlist.PoisDistrictListFragment
import com.antoniomy82.poi_challenge.utils.PoisUtils


class HomeDistrictAdapter (private val districtList: ArrayList<DistrictListMockUp>, val context: Context) :
RecyclerView.Adapter<HomeDistrictAdapter.ViewHolder>() {


    //Inflate view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_home_districts,
            parent,
            false
        )
    )

    //Binding each element with object element
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterHomeDistrictsBinding.cityName.text = districtList[position].cityName
        holder.adapterHomeDistrictsBinding.nameDistrict.text = districtList[position].district
        holder.adapterHomeDistrictsBinding.imagePoi.background = districtList[position].flag?.let { ContextCompat.getDrawable(context, it) }

        holder.adapterHomeDistrictsBinding.root.setOnClickListener{
            PoisUtils.replaceFragment(PoisDistrictListFragment(null,districtList[position].cityName, position), (context as AppCompatActivity).supportFragmentManager)
            Log.d("position", position.toString())
        }
    }


    override fun getItemCount(): Int {
        return districtList.size
    }

    class ViewHolder(val adapterHomeDistrictsBinding: AdapterHomeDistrictsBinding) :
        RecyclerView.ViewHolder(adapterHomeDistrictsBinding.root)

}
