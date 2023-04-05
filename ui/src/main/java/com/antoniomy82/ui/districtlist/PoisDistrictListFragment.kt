package com.antoniomy82.ui.districtlist

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.antoniomy82.data.model.District
import com.antoniomy82.mycities.ui.R
import com.antoniomy82.mycities.ui.databinding.FragmentDistrictListBinding
import com.antoniomy82.data.DistrictsRepository
import com.antoniomy82.ui.viewmodel.PoisViewModel


class PoisDistrictListFragment(
    private val mDistrict: District? = null,
    var cityName: String? = null,
    private val urlID: Int
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

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        poisViewModel = ViewModelProvider(this)[PoisViewModel::class.java]

        activity?.let {
            context?.let { it1 ->
                poisViewModel?.setDistrictListFragmentBinding(
                    it,
                    it1,
                    view,
                    savedInstanceState,
                    fragmentDistrictListBinding,
                    urlID
                )
            }
        }

        poisViewModel?.selectedCity = cityName.toString()

        poisViewModel?.setPoisListUI()

        when (mDistrict) {
            null -> {
                lifecycleScope.launchWhenCreated {
                    DistrictsRepository("$urlID").remoteDistrictRepository.collect { retrieveDistrict ->
                        setObserverIntoViewModel(retrieveDistrict)
                    }
                }
            }
            else -> {
                poisViewModel?.retrieveDistrict = mDistrict
                poisViewModel?.setDistrictListRecyclerViewAdapter(mDistrict)
                poisViewModel?.setTittleFromAdapter(
                    mDistrict.name.toString(),
                    mDistrict.pois?.size.toString()
                )
                fragmentDistrictListBinding.poisVM = poisViewModel
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    private fun setObserverIntoViewModel(retrieveDistrict: District) {

        retrieveDistrict.let {
            poisViewModel?.retrieveDistrict = it
            poisViewModel?.setDistrictListRecyclerViewAdapter(it)
            poisViewModel?.setTittleFromAdapter(
                it.name.toString(),
                it.pois?.size.toString()
            )
            fragmentDistrictListBinding.poisVM = poisViewModel //update VM content

        }
    }
}