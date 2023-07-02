package com.example.wassalniDR.fragments

import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.example.wassalniDR.R
import com.example.wassalniDR.adapters.TripsAdapter
import com.example.wassalniDR.data.uiState.TripUiState
import com.example.wassalniDR.database.TripsRetrofit
import com.example.wassalniDR.databinding.FragmentTripsBinding
import com.example.wassalniDR.databinding.HeaderBinding
import com.example.wassalniDR.datasource.TripsDataSource
import com.example.wassalniDR.repo.TripRepositry
import com.example.wassalniDR.util.Constant
import com.example.wassalniDR.viewModels.TripsViewModel
import com.google.android.material.navigation.NavigationView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val TAG = "TripsFragment"

class TripsFragment : Fragment() {

    private lateinit var tripsDataSource: TripsDataSource
    private lateinit var binding: FragmentTripsBinding
    private lateinit var tripsViewModel: TripsViewModel
    private lateinit var repo: TripRepositry
    private lateinit var adapter: TripsAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var headerBinding: HeaderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripsBinding.inflate(inflater)
        val retrofit = Retrofit.Builder().baseUrl(Constant.BASEURL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
                )
            ).build()
        val tripRetrofit = retrofit.create(TripsRetrofit::class.java)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        tripsDataSource = TripsDataSource(tripRetrofit, sharedPreferences)
        repo = TripRepositry(tripsDataSource)
        tripsViewModel = TripsViewModel(repo)
        adapter = TripsAdapter()
        binding.rvTrips.adapter = adapter
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
//        drawerLayout=binding.drawerLayout

        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }

        headerBinding = HeaderBinding.inflate(layoutInflater)
        binding.navView.addHeaderView(headerBinding.root)
        binding.navView.itemIconTintList = null

        handleNavigationView()

        val token = sharedPreferences.getString("token", "")
        Log.e("TAG", "token equals:$token")
        tripsViewModel.getTrips(token!!)
        handleTripsLiveData()

        binding.errorState.retry.setOnClickListener {
            tripsViewModel.getTrips(token)
        }
        getDriverData()

    }

    private fun getDriverData() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val driver = tripsViewModel.retrieveDriverData()
                withContext(Dispatchers.Main) {
                    Glide.with(requireActivity()).load(driver.image).circleCrop()
                        .into(headerBinding.driverImg)
                    headerBinding.driverEmail.text = driver.email
                    headerBinding.driverName.text = driver.name

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun handleTripsLiveData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripsViewModel.state.collect {
                    when (it) {
                        is TripUiState.Loading -> {
                            showLoadingState()
                        }

                        is TripUiState.Success -> {
                            showSuccessState()
                            adapter.setData(it.trips)
                        }

                        is TripUiState.Error -> {
                            showErrorState()
                            Toast.makeText(requireContext(), it.errorMsg, Toast.LENGTH_LONG).show()
                        }

                        is TripUiState.Empty -> {
                            showEmptyState()
                        }
                    }
                }
            }
        }
    }

    private fun showLoadingState() {
        binding.errorState.root.visibility = View.GONE
        binding.emptyState.root.visibility = View.GONE
        binding.loadingState.root.visibility = View.VISIBLE

    }

    private fun showSuccessState() {
        binding.loadingState.root.visibility = View.GONE
        binding.errorState.root.visibility = View.GONE
        binding.emptyState.root.visibility = View.GONE
        binding.rvTrips.visibility = View.VISIBLE

    }

    private fun showErrorState() {
        binding.loadingState.root.visibility = View.GONE
        binding.emptyState.root.visibility = View.GONE
        binding.rvTrips.visibility = View.GONE
        binding.errorState.root.visibility = View.VISIBLE
    }

    private fun showEmptyState() {
        binding.loadingState.root.visibility = View.GONE
        binding.errorState.root.visibility = View.GONE
        binding.rvTrips.visibility = View.GONE
        binding.emptyState.root.visibility = View.VISIBLE
    }


    private fun handleNavigationView() {
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.finished_trips -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    findNavController().navigate(R.id.action_tripsFragment_to_finshedTripsFragment)

                }

                R.id.supporter -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    findNavController().navigate(R.id.action_tripsFragment_to_supporterFragment)
                }

                R.id.rating -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    findNavController().navigate(R.id.action_tripsFragment_to_ratingFragment)

                }

                R.id.balance -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    findNavController().navigate(R.id.action_tripsFragment_to_balanceFragment)
                }

                R.id.log_out -> {
                    sharedPreferences
                        .edit()
                        .putBoolean("isLoggedIn", false)
                        .putString("token", "")
                        .apply()
                    requireActivity().setResult(AppCompatActivity.RESULT_OK)
                    requireActivity().finish()
                }

            }
            true
        }
    }
}