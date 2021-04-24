package com.antoniomy82.poi_challenge.utils

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.antoniomy82.poi_challenge.R

class PoisUtils {

    companion object {

        fun replaceFragment(fragment: Fragment? ,fragmentManager: FragmentManager) {
            try {
                val transaction = fragmentManager.beginTransaction()
                fragment?.let { transaction.replace(R.id.frame_container, it) }
                transaction.commit()
            } catch (e: Exception) {
                Log.e("__replaceFragment", e.toString())
            }
        }
    }
}