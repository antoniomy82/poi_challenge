package com.antoniomy82.ui.homedistrict

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy82.data.model.CitiesListHome
import com.antoniomy82.mycities.ui.R
import com.antoniomy82.mycities.ui.databinding.AdapterHomeDistrictsBinding
import com.antoniomy82.ui.districtlist.PoisDistrictListFragment
import com.antoniomy82.ui.replaceFragment


class HomeDistrictAdapter(
    private val districtList: ArrayList<CitiesListHome>,
    private val context: Context
) : RecyclerView.Adapter<HomeDistrictAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_home_districts,
            parent, false)
    )

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterHomeDistrictsBinding.apply {
            cityName.text = districtList[position].cityName
            nameDistrict.text = districtList[position].district
            imagePoi.background = districtList[position].flag?.let { ContextCompat.getDrawable(context, it) }

            root.setOnClickListener {
                replaceFragment(districtList[position].urlId?.let { it1 ->
                    PoisDistrictListFragment(null, districtList[position].cityName, it1)
                }, (context as AppCompatActivity).supportFragmentManager)
            }
        }
    }


    override fun getItemCount() = districtList.size

    class ViewHolder(val adapterHomeDistrictsBinding: AdapterHomeDistrictsBinding) : RecyclerView.ViewHolder(adapterHomeDistrictsBinding.root)

}
