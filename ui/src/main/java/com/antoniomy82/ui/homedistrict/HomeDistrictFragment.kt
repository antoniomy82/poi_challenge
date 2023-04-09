package com.antoniomy82.ui.homedistrict

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy82.data.model.CitiesListHome
import com.antoniomy82.mycities.ui.R
import com.antoniomy82.mycities.ui.databinding.FragmentHomeDistrictBinding
import com.antoniomy82.ui.viewmodel.PoisViewModel
import kotlin.system.exitProcess

class HomeDistrictFragment : Fragment() {

    private var poisViewModel: PoisViewModel? = null
    private var fragmentHomeDistrictBinding: FragmentHomeDistrictBinding? = null

    private val madName = "Madrid"
    private val sevName = "Sevilla"
    private val bcnName = "Barcelona"

    private val homeCities: List<CitiesListHome> = listOf(
        CitiesListHome(madName, "Lavapíes", R.mipmap.ic_madrid_round, 1),
        CitiesListHome(madName, "Centro", R.mipmap.ic_madrid_round, 2),
        CitiesListHome(madName, "Malasaña", R.mipmap.ic_madrid_round, 3),
        CitiesListHome(madName, "Chueca", R.mipmap.ic_madrid_round, 5),
        CitiesListHome(madName, "Huertas", R.mipmap.ic_madrid_round, 6),
        CitiesListHome(sevName, "Alfalfa, Casco Antiguo", R.mipmap.ic_sevilla_round, 7),
        CitiesListHome(sevName, "Arenal, Museo", R.mipmap.ic_sevilla_round, 8),
        CitiesListHome(sevName, "Macarena, S.Luis, S.Vicente", R.mipmap.ic_sevilla_round, 9),
        CitiesListHome(sevName, "Santa Cruz, Juderia", R.mipmap.ic_sevilla_round, 10),
        CitiesListHome(sevName, "Triana, Los Remedios", R.mipmap.ic_sevilla_round, 11),
        CitiesListHome(bcnName, "Barceloneta, Poble Nou", R.mipmap.ic_barcelona_round, 12),
        CitiesListHome(bcnName, "El born, Ribera", R.mipmap.ic_barcelona_round, 13),
        CitiesListHome(bcnName, "Gótico", R.mipmap.ic_barcelona_round, 14),
        CitiesListHome(bcnName, "Raval Poble Sec", R.mipmap.ic_barcelona_round, 15),
        CitiesListHome(bcnName, "Eixample, Gracia", R.mipmap.ic_barcelona_round, 16)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentHomeDistrictBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_district, container, false)
        return fragmentHomeDistrictBinding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        poisViewModel = ViewModelProvider(this)[PoisViewModel::class.java]
        fragmentHomeDistrictBinding?.poisVM = poisViewModel

        setUI()
        setHomeRecyclerViewAdapter()
    }

    private fun setUI() {
        val headerTitle = view?.findViewById<View>(R.id.headerTitle) as TextView
        headerTitle.text = context?.getString(R.string.home_title)

        view?.findViewById<View>(R.id.headerBack)?.apply {
            setBackgroundResource(R.drawable.baseline_close_24)

            setOnClickListener {
                activity?.finish()
                exitProcess(0)
            }
        }
        poisViewModel?.poisCount?.value = homeCities.size.toString()  //Set counter
    }

    private fun setHomeRecyclerViewAdapter() {
        val recyclerView: RecyclerView = view?.findViewById(R.id.rvHome) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = context?.let { HomeDistrictAdapter(homeCities, it) }
    }
}