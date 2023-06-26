package com.example.wassalniDR.activites

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
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
    lateinit var navigationView: NavigationView
    lateinit var drawerLayout :DrawerLayout



    @Inject
    lateinit var permission: Permission
//
//    val binding by lazy {
//        ActivityDriverBinding.inflate(layoutInflater)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDriverBinding.inflate(layoutInflater)
        navigationView=binding.nav
        drawerLayout=binding.drawerLayout
        setContentView(binding.root)


        handleNavigationView()
    }

    private fun handleNavigationView() {
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.finished_trips -> {
                    drawerLayout.closeDrawer(GravityCompat.START)

                    fragmentR(FinshedTripsFragment())
                }
                R.id.supporter -> {
                    drawerLayout.closeDrawer(GravityCompat.START)

                    fragmentR(SupporterFragment())
                }
                R.id.rating -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    Toast.makeText(applicationContext, "Comment", Toast.LENGTH_SHORT).show()
                    fragmentR(RatingFragment())
                }
                R.id.log_out->{

                }

            }
            true
        }
    }



    private fun fragmentR(fragment:Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.driver_nav_host, fragment)
            .addToBackStack(null)
            .commit()
    }

}