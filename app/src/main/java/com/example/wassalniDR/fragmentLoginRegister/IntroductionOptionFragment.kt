package com.example.wassalniDR.fragmentLoginRegister

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wassalniDR.R
import com.example.wassalniDR.databinding.FragmentOptionBinding
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroductionOptionFragment:Fragment(R.layout.fragment_option) {
    private lateinit var binding:FragmentOptionBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentOptionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnLoginAccountOptions.setOnClickListener {
           // findNavController().navigate(R.id.action_introductionOptionFragment_to_Login)
        }
    }
}