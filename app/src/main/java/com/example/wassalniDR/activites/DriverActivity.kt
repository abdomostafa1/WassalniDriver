package com.example.wassalniDR.activites

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.wassalniDR.databinding.ActivityDriverBinding
import com.example.wassalniDR.util.Permission
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DriverActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var permission: Permission

    val binding by lazy {
        ActivityDriverBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}