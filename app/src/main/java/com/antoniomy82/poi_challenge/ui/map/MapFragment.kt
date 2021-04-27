package com.antoniomy82.poi_challenge.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.antoniomy82.poi_challenge.R
import com.antoniomy82.poi_challenge.databinding.FragmentMapBinding
import com.antoniomy82.poi_challenge.viewmodel.PoisViewModel


class MapFragment(val poisVM: PoisViewModel, var cityName:String?=null) : Fragment() {

    private var fragmentMapBinding: FragmentMapBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentMapBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_map,
            container,
            false
        )
        return fragmentMapBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Set fragment parameters in vm
        activity?.let {
            context?.let { it1 ->
                fragmentMapBinding?.let { it2 ->
                    poisVM.setMapsFragmentBinding(
                        it, it1, view,
                        it2, savedInstanceState
                    )
                }
            }
        }
        poisVM.setMapsUI()
        poisVM.selectedCity= cityName.toString()
    }

}