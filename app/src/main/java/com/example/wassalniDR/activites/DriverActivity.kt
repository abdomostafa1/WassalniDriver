package com.example.wassalniDR.activites

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.wassalniDR.R
import com.example.wassalniDR.databinding.ActivityDriverBinding
import com.example.wassalniDR.databinding.ActivityLoginRegisterBinding
import com.example.wassalniDR.fragments.FinshedTripsFragment
import com.example.wassalniDR.fragments.RatingFragment
import com.example.wassalniDR.fragments.SupporterFragment
import com.example.wassalniDR.util.Permission
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DriverActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var binding:ActivityDriverBinding



    @Inject
    lateinit var permission: Permission
//
//    val binding by lazy {
//        ActivityDriverBinding.inflate(layoutInflater)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}