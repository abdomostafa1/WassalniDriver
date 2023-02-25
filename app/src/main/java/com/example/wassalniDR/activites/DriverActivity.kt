package com.example.wassalniDR.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wassalniDR.R
import com.example.wassalniDR.databinding.ActivityDriverBinding

class DriverActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityDriverBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController=findNavController(R.id.driver_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)

    }
}