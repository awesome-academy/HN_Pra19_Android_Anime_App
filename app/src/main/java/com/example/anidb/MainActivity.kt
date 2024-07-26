package com.example.anidb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.anidb.screen.home.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .addToBackStack(HomeFragment::javaClass.name)
            .replace(R.id.layoutContainer, HomeFragment())
            .commit()
    }
}
