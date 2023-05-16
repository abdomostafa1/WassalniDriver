package com.example.wassalniDR.activites

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.wassalniDR.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        isLoggedIn()
    }

    private val loginLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
        if (result.resultCode== RESULT_CANCELED)
            finish()
        else
            isLoggedIn()
    }

    private val mainLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        if (result.resultCode== RESULT_CANCELED)
            finish()
    }
    private fun isLoggedIn() {
        val isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false)
        if (isLoggedIn)
            openDriverActivity()
        else
            openLoginActivity()
    }

    private fun openDriverActivity() {
        val intent=Intent(this,DriverActivity::class.java)
            mainLauncher.launch(intent)
        
    }
    private fun openLoginActivity() {
        val intent=Intent(this,LoginActivity::class.java)
            loginLauncher.launch(intent)
    }
}