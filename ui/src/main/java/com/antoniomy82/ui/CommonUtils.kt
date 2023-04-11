package com.antoniomy82.ui

import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.antoniomy82.mycities.ui.R
import com.antoniomy82.ui.viewmodel.PoisViewModel

fun replaceFragment(fragment: Fragment?, fragmentManager: FragmentManager) {
    try {
        val transaction = fragmentManager.beginTransaction()
        fragment?.let { transaction.replace(R.id.frame_container, it) }
        transaction.commit()
    } catch (e: Exception) {
        Log.e("__replaceFragment", e.toString())
    }
}

fun getTimeResult(millisUntilFinished: Long) =
    "${(millisUntilFinished / 1000 / 60).toString().padStart(2, '0')}:" +
            "${(millisUntilFinished / 1000 % 60).toString().padStart(2, '0')} "

fun mediaProgress(totalDuration: Long, viewModel: PoisViewModel): CountDownTimer {
    val timer = object : CountDownTimer(totalDuration, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            viewModel.apply {
                remainingTime.value = getTimeResult(millisUntilFinished)
                popUpBinding?.vm = getVM() //Update the view with dataBinding
                //TODO: Cambiar por un flow
            }
        }

        override fun onFinish() = viewModel.buttonStop()
    }
    timer.start()
    return timer
}