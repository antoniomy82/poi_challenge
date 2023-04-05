package com.antoniomy82.ui.districtlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy82.data.model.District
import com.antoniomy82.mycities.ui.R
import com.antoniomy82.mycities.ui.databinding.AdapterPoisDistrictListBinding


import com.antoniomy82.ui.detail.DetailFragment
import com.antoniomy82.ui.replaceFragment
import com.antoniomy82.ui.viewmodel.PoisViewModel
import com.bumptech.glide.Glide

class PoisDistrictListAdapter(private val poisVm: PoisViewModel, private val mDistrict: District, private val context: Context) :
    RecyclerView.Adapter<PoisDistrictListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_pois_district_list,
            parent, false)
    )


    //Binding each element with object element
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterPoisDistrictListBinding.apply {
            poisVm = poisVm
            namePoi.text = mDistrict.pois?.get(position)?.name

            // if( mDistrict.pois?.get(position)?.likesCount==null) holder.adapterPoisDistrictListBinding.likeQty.text = "0"
            // else holder.adapterPoisDistrictListBinding.likeQty.text = mDistrict.pois?.get(position)?.likesCount.toString()
        }

        holder.adapterPoisDistrictListBinding.root.setOnClickListener {
            poisVm.popUpLocation = 0
            replaceFragment(mDistrict.pois?.get(position)?.let { it1 ->
                DetailFragment(it1, poisVm)
            }, (context as AppCompatActivity).supportFragmentManager)
        }

        //Set image
        if (mDistrict.pois?.get(position)?.image?.url != null) Glide.with(context).load(mDistrict.pois?.get(position)?.image?.url).into(holder.adapterPoisDistrictListBinding.imagePoi)

    }

    override fun getItemCount() = mDistrict.pois?.size ?:0

    class ViewHolder(val adapterPoisDistrictListBinding: AdapterPoisDistrictListBinding) : RecyclerView.ViewHolder(adapterPoisDistrictListBinding.root)

}
