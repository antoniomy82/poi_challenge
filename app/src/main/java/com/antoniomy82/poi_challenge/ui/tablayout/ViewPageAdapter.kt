package com.antoniomy82.poi_challenge.ui.tablayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.antoniomy82.poi_challenge.viewmodel.PoisViewModel

@Suppress("DEPRECATION")
class ViewPageAdapter(fm: FragmentManager, var totalTabs: Int, private val poisViewModel: PoisViewModel) : FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {

     return when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                EventsFragment(poisViewModel)
            }
            1 -> {
              PresentFragment(poisViewModel)
            }
            else -> getItem(position)
        }

    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}