package com.example.wassalniDR.fragmentLoginRegister

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.wassalniDR.R
import com.example.wassalniDR.activites.DriverActivity
import com.example.wassalniDR.data.Drivers
import com.example.wassalniDR.util.DriversRemoteDataSource
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.wassalniDR.databinding.FragmentLoginBinding
import com.example.wassalniDR.repo.LoginRepositry
import com.example.wassalniDR.util.Constant.BASEURL
import com.example.wassalniDR.util.DriversRetrofit
import com.example.wassalniDR.util.LoginUiState
import com.example.wassalniDR.viewModels.LoginViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


private val TAG="SignInFragments"
class LoginFragment: androidx.fragment.app.Fragment() {
    private lateinit var remoteDatabase : DriversRemoteDataSource
    private lateinit var binding: FragmentLoginBinding
    private lateinit var  loginViewModel: LoginViewModel
    private lateinit var repo:LoginRepositry
    private lateinit var retrofit:DriversRetrofit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentLoginBinding.inflate(inflater)
        retrofit= Retrofit.Builder().baseUrl(BASEURL).addConverterFactory(MoshiConverterFactory.create())
            .build().create(DriversRetrofit::class.java)
        remoteDatabase=DriversRemoteDataSource(requireActivity().applicationContext,retrofit)
        repo= LoginRepositry(remoteDatabase)
        loginViewModel= LoginViewModel(repo)
        return binding.root
    }




    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {

            loginViewModel.loginUiState.collect {
                when (it) {
                    is LoginUiState.Loading -> {

                    }
                    is LoginUiState.LoginSuccess -> {
                        inntent()
                    }
                    is LoginUiState.Error -> {
                        Toast.makeText(context, it.errorMsg, Toast.LENGTH_LONG).show()
                    }
                    else -> {

                    }
                }
            }
        }
        binding.btnLogin.setOnClickListener {
            val email=binding.tvEmailLogin.text.toString()
            val pass=binding.tvPassLogin.text.toString()
            loginViewModel.viewModelScope.launch(Dispatchers.IO) {
                loginViewModel.makeLoginRequest(email,pass)
            }

        }


    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun inntent()
    {
        val intent = Intent(requireActivity(),DriverActivity::class.java)
        startActivity(intent)
//        val intent= android.content.Intent(context, DriverActivity::class.java).also { intent->
//            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK or android.content.Intent.FLAG_ACTIVITY_NEW_TASK)//Like shared Pref to still loging
    }

    }
