package com.example.wassalniDR.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.wassalniDR.R
import com.example.wassalniDR.databinding.ActivityLoginRegisterBinding
import com.example.wassalniDR.util.LoginUiState
import com.example.wassalniDR.viewModels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityLoginRegisterBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenStarted {

            loginViewModel.loginUiState.collect {
                when (it) {
                    is LoginUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is LoginUiState.LoginSuccess -> {
                        binding.progressBar.visibility = View.GONE
                        onSuccessfulLogin()
                    }
                    is LoginUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, getString(R.string.incorrect_email_or_password), Toast.LENGTH_LONG).show()
                    }
                    else -> {

                    }
                }
            }

        }
        binding.btnLogin.setOnClickListener {
            val email = binding.tvEmailLogin.text.toString()
            val pass = binding.tvPassLogin.text.toString()
            loginViewModel.makeLoginRequest(email, pass)
        }
    }

    private fun onSuccessfulLogin() {
        setResult(RESULT_OK)
        finish()
    }
}