package com.antoniomy82.poi_challenge.ui.tablayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.antoniomy82.poi_challenge.R
import com.antoniomy82.poi_challenge.viewmodel.PoisViewModel
import com.google.android.material.tabs.TabLayout

class TabLayoutFragment(val poisViewModel: PoisViewModel) : Fragment() {

    var rootView:View ?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_tab_layout, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager: ViewPager?= rootView?.findViewById(R.id.viewPager)
        val tabLayout: TabLayout?= rootView?.findViewById(R.id.tabLayout)

        tabLayout?.setupWithViewPager(viewPager)

        val adapterPager: ViewPageAdapter?= tabLayout?.tabCount?.let {
            ViewPageAdapter(
                childFragmentManager,
                it,
                poisViewModel
            )
        }

        viewPager?.adapter=adapterPager

        tabLayout?.addTab(tabLayout.newTab().setText(R.string.event_tap))
        tabLayout?.addTab(tabLayout.newTab().setText(R.string.present_tap))
        viewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager?.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }
}