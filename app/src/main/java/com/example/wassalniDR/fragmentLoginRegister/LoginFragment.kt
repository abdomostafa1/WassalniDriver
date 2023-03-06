package com.example.wassalniDR.fragmentLoginRegister

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.wassalniDR.R
import com.example.wassalniDR.activites.DriverActivity
import com.example.wassalniDR.data.Drivers
import com.example.wassalniDR.util.DriversRemoteDataSource
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.wassalniDR.databinding.FragmentLoginBinding
import com.example.wassalniDR.viewModels.LoginViewModel


private val TAG="SignInFragments"
class LoginFragment: androidx.fragment.app.Fragment() {
    private lateinit var remoteDatabase : DriversRemoteDataSource
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentLoginBinding.inflate(inflater)
        remoteDatabase=DriversRemoteDataSource(requireActivity())
        return binding.root
    }




    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
//            val driver=getDriverData()
//             remoteDatabase.createDriver(driver.email)
            loginViewModel.email= getDriverData().email
            loginViewModel.password=getDriverData().password
//            loginViewModel.makeLoginRequest()
            Intent()
        }


    }
    private fun getDriverData(): Drivers {
        val email = binding.tvEmailLogin.text.toString()
        val pass=binding.tvPassLogin.text.toString()
        val driver=Drivers(email,pass)
        return driver
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun Intent()
    {
        val intent= android.content.Intent(context, DriverActivity::class.java).also { intent->
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK or android.content.Intent.FLAG_ACTIVITY_NEW_TASK)//Like shared Pref to still loging
        }
        startActivity(intent)
    }
}