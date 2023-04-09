package com.antoniomy82.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.antoniomy82.mycities.ui.R
import com.antoniomy82.ui.homedistrict.HomeDistrictFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Load Home fragment
        replaceFragment(HomeDistrictFragment(), supportFragmentManager)
    }

    //Disable automatic back button
    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "Toast.makeText(this, R.string.toast_no, Toast.LENGTH_LONG).show()",
            "android.widget.Toast",
            "com.antoniomy82.mycities.ui.R",
            "android.widget.Toast"
        )
    )
    override fun onBackPressed() {
        Toast.makeText(this, R.string.toast_no, Toast.LENGTH_LONG).show()
    }
}