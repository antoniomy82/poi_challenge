package com.antoniomy82.poi_challenge.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.antoniomy82.poi_challenge.R
import com.antoniomy82.poi_challenge.utils.PoisUtils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Load Main fragment
        PoisUtils.replaceFragment(PoisDistrictListFragment(), supportFragmentManager)
    }

    //Disable automatic back button
    override fun onBackPressed() {
        Toast.makeText(this, R.string.toast_no, Toast.LENGTH_LONG).show()
    }
}