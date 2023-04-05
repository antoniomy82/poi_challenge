package com.antoniomy82.ui

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.antoniomy82.mycities.ui.R

fun replaceFragment(fragment: Fragment?, fragmentManager: FragmentManager) {
    try {
        val transaction = fragmentManager.beginTransaction()
        fragment?.let { transaction.replace(R.id.frame_container, it) }
        transaction.commit()
    } catch (e: Exception) {
        Log.e("__replaceFragment", e.toString())
    }
}

