package com.antoniomy82.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.antoniomy82.data.model.Pois
import com.antoniomy82.mycities.ui.R
import com.antoniomy82.mycities.ui.databinding.PopUpPoisDetailBinding
import com.antoniomy82.ui.viewmodel.PoisViewModel


class DetailFragment(private val mPoi: Pois, private val mVm: PoisViewModel) : Fragment() {

    private var poisViewModel: PoisViewModel? = null
    private var popUpPoisDetailBinding: PopUpPoisDetailBinding?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        popUpPoisDetailBinding = DataBindingUtil.inflate(inflater, R.layout.pop_up_pois_detail, container, false)
        return popUpPoisDetailBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        poisViewModel = ViewModelProvider(this)[PoisViewModel::class.java]

        poisViewModel= mVm
        poisViewModel?.popUpBinding=popUpPoisDetailBinding

        popUpPoisDetailBinding?.let { poisViewModel?.popUpDetail(mPoi, context, it) }
    }

}