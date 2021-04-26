package com.antoniomy82.poi_challenge.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.antoniomy82.poi_challenge.R
import com.antoniomy82.poi_challenge.databinding.FragmentMainBinding
import com.antoniomy82.poi_challenge.model.District
import com.antoniomy82.poi_challenge.model.DistrictsRepository
import com.antoniomy82.poi_challenge.viewmodel.PoisViewModel


class PoisDistrictListFragment(val mDistrict: District? = null) : Fragment() {

    private var poisViewModel: PoisViewModel? = null
    private lateinit var fragmentMainBinding: FragmentMainBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMainBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return fragmentMainBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        poisViewModel = ViewModelProvider(this).get(PoisViewModel::class.java)

        activity?.let {
            context?.let { it1 ->
                poisViewModel?.setMainFragmentBinding(
                    it,
                    it1,
                    view,
                    savedInstanceState
                )
            }
        }

        poisViewModel?.setMainUI()

        if (mDistrict == null) {
            DistrictsRepository().getDistrict1().observe(viewLifecycleOwner) { retrieveDistrict ->
                if (retrieveDistrict != null) {
                    poisViewModel?.retrieveDistrict = retrieveDistrict
                    poisViewModel?.setRecyclerViewAdapter(retrieveDistrict)
                    poisViewModel?.setTittleFromAdapter(
                        retrieveDistrict.name.toString(),
                        retrieveDistrict.pois?.size.toString()
                    )
                    fragmentMainBinding.poisVM = poisViewModel //update VM content

                }
            }
        } else {
            poisViewModel?.retrieveDistrict = mDistrict
            poisViewModel?.setRecyclerViewAdapter(mDistrict)
            poisViewModel?.setTittleFromAdapter(
                mDistrict.name.toString(),
                mDistrict.pois?.size.toString()
            )
            fragmentMainBinding.poisVM = poisViewModel
        }
    }


}