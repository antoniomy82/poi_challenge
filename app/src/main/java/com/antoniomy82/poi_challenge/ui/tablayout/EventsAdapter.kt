package com.antoniomy82.poi_challenge.ui.tablayout

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy82.poi_challenge.R
import com.antoniomy82.poi_challenge.databinding.AdapterEventsBinding
import com.antoniomy82.poi_challenge.model.News


class EventsAdapter (private val newsList: ArrayList<News>, val context: Context) :
RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    //Inflate view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_events,
            parent,
            false
        )
    )

    //Binding each element with object element
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterEventsBinding.eventName.text = newsList[position].eventName
        holder.adapterEventsBinding.dateEvent.text = newsList[position].eventDate
        holder.adapterEventsBinding.dateRound.text = newsList[position].dateRounded

    }


    override fun getItemCount(): Int {
        return newsList.size
    }

    class ViewHolder(val adapterEventsBinding: AdapterEventsBinding) :
        RecyclerView.ViewHolder(adapterEventsBinding.root)

}
