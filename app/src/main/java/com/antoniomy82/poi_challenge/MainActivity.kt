package com.antoniomy82.poi_challenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.antoniomy82.poi_challenge.model.District
import com.antoniomy82.poi_challenge.model.DistrictsRepository

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DistrictsRepository().getDistrict1()
    }
}