package com.example.wassalniDR.fragmentLoginRegister

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.example.wassalniDR.activites.DriverActivity
import com.example.wassalniDR.util.DriversRemoteDataSource
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.wassalniDR.R
import com.example.wassalniDR.databinding.FragmentLoginBinding
import com.example.wassalniDR.repo.LoginRepository
import com.example.wassalniDR.util.Constant.BASEURL
import com.example.wassalniDR.database.DriversRetrofit
import com.example.wassalniDR.util.LoginUiState
import com.example.wassalniDR.viewModels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


private val TAG = "SignInFragments"

@AndroidEntryPoint
class LoginFragment : androidx.fragment.app.Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }




}
