package com.example.wassalniDR.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wassalniDR.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }
}