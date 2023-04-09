package com.antoniomy82.ui.districtlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy82.data.DistrictsRepository
import com.antoniomy82.data.model.District
import com.antoniomy82.mycities.ui.R
import com.antoniomy82.mycities.ui.databinding.FragmentDistrictListBinding
import com.antoniomy82.ui.homedistrict.HomeDistrictFragment
import com.antoniomy82.ui.replaceFragment
import com.antoniomy82.ui.viewmodel.PoisViewModel

class PoisDistrictListFragment(
    private val mDistrict: District? = null,
    private var cityName: String? = null,
    private val urlID: Int
) : Fragment() {

    private var poisViewModel: PoisViewModel? = null
    private lateinit var fragmentDistrictListBinding: FragmentDistrictListBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentDistrictListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_district_list, container, false)
        return fragmentDistrictListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        poisViewModel = ViewModelProvider(this)[PoisViewModel::class.java]

        when (mDistrict) {
            null -> {
                lifecycleScope.launchWhenCreated {
                    DistrictsRepository("$urlID").remoteDistrictRepository.collect { retrieveDistrict ->
                        setTittleFromAdapter(isEmpty = true)
                        setObserverIntoViewModel(retrieveDistrict)
                    }
                }
            }
            else -> {
                poisViewModel?.retrieveDistrict = mDistrict
                setDistrictListRecyclerViewAdapter(mDistrict)
                setTittleFromAdapter(
                    mDistrict.name.toString(),
                    mDistrict.pois?.size.toString(),
                )
                fragmentDistrictListBinding.poisVM = poisViewModel
            }
        }

        setUI()
        context?.let { poisViewModel?.frgMainContext = it }

    }


    private fun setUI() {
        //Top bar title
        val headerTitle = view?.findViewById<View>(R.id.headerTitle) as TextView
        headerTitle.text = cityName

        //Back arrow
        view?.findViewById<View>(R.id.headerBack)?.setOnClickListener {
            replaceFragment(HomeDistrictFragment(), parentFragmentManager)
        }

    }

    private fun setObserverIntoViewModel(retrieveDistrict: District) {

        retrieveDistrict.let {
            poisViewModel?.retrieveDistrict = it
            setDistrictListRecyclerViewAdapter(it)
           if(it.name!=null) {
               setTittleFromAdapter(
                   it.name.toString(),
                   it.pois?.size.toString()
               )
           }
            fragmentDistrictListBinding.poisVM = poisViewModel //update VM content
        }
    }

    private fun setDistrictListRecyclerViewAdapter(mDistrict: District) {
        val recyclerView: RecyclerView = view?.findViewById(R.id.rvPois) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = context?.let {
            poisViewModel?.let { it1 ->
                PoisDistrictListAdapter(
                    it1,
                    mDistrict,
                    parentFragmentManager
                )
            }
        }
    }


    private fun setTittleFromAdapter(tittle: String? = null, count: String? = null, isEmpty: Boolean = false) {
        when (isEmpty) {
            true -> {
                poisViewModel?.apply {
                    districtTittle.value = fragmentDistrictListBinding.root.context?.getString(R.string.loading)
                    poisCount.value = ""
                }
                fragmentDistrictListBinding.apply {
                    progressBar.visibility = View.VISIBLE
                    mapLayout.visibility = View.GONE
                    rvPois.visibility = View.GONE
                }
            }
            false -> {
                poisViewModel?.apply {
                    districtTittle.value = tittle?.uppercase()
                    poisCount.value = count
                }
                fragmentDistrictListBinding.apply {
                    progressBar.visibility = View.GONE
                    mapLayout.visibility = View.VISIBLE
                    rvPois.visibility = View.VISIBLE
                }
            }
        }
    }
}