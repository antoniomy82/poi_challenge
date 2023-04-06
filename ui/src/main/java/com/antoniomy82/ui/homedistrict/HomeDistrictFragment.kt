package com.antoniomy82.ui.homedistrict

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.antoniomy82.mycities.ui.R
import com.antoniomy82.mycities.ui.databinding.FragmentHomeDistrictBinding
import com.antoniomy82.ui.viewmodel.PoisViewModel


class HomeDistrictFragment : Fragment() {

    private var poisViewModel: PoisViewModel? = null
    private var fragmentHomeDistrictBinding: FragmentHomeDistrictBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentHomeDistrictBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_district, container, false)
        return fragmentHomeDistrictBinding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        poisViewModel = ViewModelProvider(this)[PoisViewModel::class.java]
        fragmentHomeDistrictBinding?.poisVM = poisViewModel

        poisViewModel?.setHomeUI(view, activity, context)

    }


}