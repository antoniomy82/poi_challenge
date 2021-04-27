package com.antoniomy82.poi_challenge.ui.districtlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.antoniomy82.poi_challenge.R
import com.antoniomy82.poi_challenge.databinding.FragmentDistrictListBinding
import com.antoniomy82.poi_challenge.model.District
import com.antoniomy82.poi_challenge.model.DistrictsRepository
import com.antoniomy82.poi_challenge.viewmodel.PoisViewModel


class PoisDistrictListFragment(
    private val mDistrict: District? = null,
    var cityName: String? = null,
    private val position: Int
) : Fragment() {

    private var poisViewModel: PoisViewModel? = null
    private lateinit var fragmentDistrictListBinding: FragmentDistrictListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentDistrictListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_district_list, container, false)
        return fragmentDistrictListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        poisViewModel = ViewModelProvider(this).get(PoisViewModel::class.java)

        activity?.let {
            context?.let { it1 ->
                poisViewModel?.setDistrictListFragmentBinding(
                    it,
                    it1,
                    view,
                    savedInstanceState,
                    fragmentDistrictListBinding,
                    position
                )
            }
        }



        poisViewModel?.selectedCity = cityName.toString()

        poisViewModel?.setPoisListUI()


        if (mDistrict == null) {
            when (position) {
                0 -> {
                    context?.let {
                        DistrictsRepository().getDistrict(it)
                            ?.observe(viewLifecycleOwner) { retrieveDistrict ->
                                setObserverIntoViewModel(retrieveDistrict)
                            }
                    }
                }
                1 -> {
                    context?.let {
                        DistrictsRepository().getDistrict1(it)
                            ?.observe(viewLifecycleOwner) { retrieveDistrict ->
                                setObserverIntoViewModel(retrieveDistrict)
                            }
                    }
                }
                2 -> {
                    context?.let {
                        DistrictsRepository().getDistrict2(it)
                            ?.observe(viewLifecycleOwner) { retrieveDistrict ->
                                setObserverIntoViewModel(retrieveDistrict)
                            }
                    }
                }

                3 -> {
                    context?.let {
                        DistrictsRepository().getDistrict3(it)
                            ?.observe(viewLifecycleOwner) { retrieveDistrict ->
                                setObserverIntoViewModel(retrieveDistrict)
                            }
                    }
                }

                4 -> {
                    context?.let {
                        DistrictsRepository().getDistrict4(it)
                            ?.observe(viewLifecycleOwner) { retrieveDistrict ->
                                setObserverIntoViewModel(retrieveDistrict)
                            }
                    }
                }
                5 -> {
                    context?.let {
                        DistrictsRepository().getDistrict5(it)
                            ?.observe(viewLifecycleOwner) { retrieveDistrict ->
                                setObserverIntoViewModel(retrieveDistrict)
                            }
                    }
                }

                6 -> {
                    context?.let {
                        DistrictsRepository().getDistrict6(it)
                            ?.observe(viewLifecycleOwner) { retrieveDistrict ->
                                setObserverIntoViewModel(retrieveDistrict)
                            }
                    }
                }

            }

        } else {
            poisViewModel?.retrieveDistrict = mDistrict
            poisViewModel?.setDistrictListRecyclerViewAdapter(mDistrict)
            poisViewModel?.setTittleFromAdapter(
                mDistrict.name.toString(),
                mDistrict.pois?.size.toString()
            )
            fragmentDistrictListBinding.poisVM = poisViewModel
        }
    }


    private fun setObserverIntoViewModel(retrieveDistrict: District) {

        if (retrieveDistrict != null) {
            poisViewModel?.retrieveDistrict = retrieveDistrict
            poisViewModel?.setDistrictListRecyclerViewAdapter(retrieveDistrict)
            poisViewModel?.setTittleFromAdapter(
                retrieveDistrict.name.toString(),
                retrieveDistrict.pois?.size.toString()
            )
            fragmentDistrictListBinding.poisVM = poisViewModel //update VM content

        }
    }
}