package com.example.wassalniDR.activites

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.wassalniDR.R
import com.example.wassalniDR.databinding.ActivityDriverBinding



class DriverActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    val binding by lazy {
        ActivityDriverBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController=findNavController(R.id.driver_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(applicationContext)




    }
}