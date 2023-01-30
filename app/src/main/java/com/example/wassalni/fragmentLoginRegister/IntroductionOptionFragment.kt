package com.example.wassalni.fragmentLoginRegister

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wassalni.R
import com.example.wassalni.databinding.FragmentOptionBinding


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

        binding.btnJoinUsAccountOptions.setOnClickListener {
            val uri: Uri =
                Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSeTatcZ_nwXopwh2L1BRbtGDskuZomF8Ukwny9heXZUATMHOw/formResponse?edit2=2_ABaOnueOJXT5Pu1F-RbWvUL0nW2AGgjqMDfdnOBtOkdfD4worfqoXgcYmMSgVKh0Sg") // missing 'http://' will cause crashed
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
        }
        binding.btnLoginAccountOptions.setOnClickListener {
            findNavController().navigate(R.id.action_introductionOption_to_loginFragment)
        }
    }
}