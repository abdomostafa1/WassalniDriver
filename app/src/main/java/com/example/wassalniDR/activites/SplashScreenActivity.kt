package com.example.wassalniDR.activites

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import com.example.wassalniDR.R
import com.example.wassalniDR.fragmentLoginRegister.LoginFragment
import com.example.wassalniDR.fragments.HomeFragment

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        lateinit var sharedPreferences: SharedPreferences

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val isLoggedIn=sharedPreferences.getBoolean("isLoggedIn", false)
        if(!isLoggedIn)
        {
            val intent= Intent(applicationContext,LoginRegisterActivity::class.java)
            startActivity(intent)
        }else{
            val intent= Intent(applicationContext, DriverActivity::class.java)
            startActivity(intent)
        }
    }
}