package com.example.wassalniDR.activites

import android.R
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.wassalniDR.databinding.ActivityDriverBinding


class DriverActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences


    val binding by lazy {
        ActivityDriverBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        val navController=findNavController(R.id.driver_host_fragment)
        val navHostFragment =
            supportFragmentManager.findFragmentById(com.example.wassalniDR.R.id.driver_host_fragment) as NavHostFragment?
        val navController = navHostFragment!!.navController
        binding.bottomNavigation.setupWithNavController(navController)
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(applicationContext)
        sharedPreferences.getString("token","")




    }
}