package com.antoniomy82.poi_challenge.ui.tablayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.antoniomy82.poi_challenge.R
import com.antoniomy82.poi_challenge.model.News
import com.antoniomy82.poi_challenge.viewmodel.PoisViewModel
import java.util.*

class EventsFragment(val poisViewModel: PoisViewModel) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Mocked news, because there isn´t any news in the repository
        val mockNews= ArrayList<News>()
        mockNews.add(News("(MOCK) FIESTA DE PRIMAVERA","23 de Abril de 2020", "23 ABR"))
        mockNews.add(News("(MOCK) EVENT TOTAL","12 de Mayo de 2021", "12 MAY"))
        mockNews.add(News("(MOCK) FIESTA FINAL COVID","13 de Junio de 2022", "22 JUN"))

        context?.let { activity?.let { it1 ->
            poisViewModel.setEventsRecyclerViewAdapter(mockNews, it,view,
                it1
            )
        } }
    }

}