package com.example.wassalniDR.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.PreferenceManager
import com.example.wassalniDR.R
import com.example.wassalniDR.adapters.SupportAdapter
import com.example.wassalniDR.data.uiState.TripUiState
import com.example.wassalniDR.database.TripsRetrofit
import com.example.wassalniDR.databinding.FragmentSupporterBinding
import com.example.wassalniDR.datasource.SupportDataSource
import com.example.wassalniDR.repo.SupportRepository
import com.example.wassalniDR.util.Constant
import com.example.wassalniDR.util.OnDialogSubmitListener
import com.example.wassalniDR.util.OnItemClickListner
import com.example.wassalniDR.viewModels.SupportViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val TAG = "SupporterFragment"
class SupporterFragment : Fragment(), OnItemClickListner, OnDialogSubmitListener {

    private lateinit var supportDataSource: SupportDataSource
    private lateinit var binding: FragmentSupporterBinding
    private lateinit var supportViewModel: SupportViewModel
    private lateinit var repo: SupportRepository
    lateinit var adapter: SupportAdapter
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSupporterBinding.inflate(inflater)
        val retrofit = Retrofit.Builder().baseUrl(Constant.BASEURL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
                )
            ).build()
        val tripRetrofit = retrofit.create(TripsRetrofit::class.java)
        supportDataSource = SupportDataSource(tripRetrofit)
        repo = SupportRepository(supportDataSource)
        supportViewModel = SupportViewModel(repo)
        adapter = SupportAdapter(this)
        binding.rvTrips.adapter = adapter
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = sharedPreferences.getString("token", "")
        Log.e("TAG", "token equals:$token")
        supportViewModel.getTrips(token!!)
        handleTripsLiveData()
    }

    private fun handleTripsLiveData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                supportViewModel.state.collect {
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
                        else -> {}
                    }
                }
            }
        }
    }

    override fun onSendMessageClick(id: String, position: Int) {
        val adapter = binding.rvTrips.adapter as SupportAdapter
        val item = adapter.getItemId(position)
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.supporter_dialog)
        val msgEt = dialog.findViewById<EditText>(R.id.dialog_message)
        val btnSubmit = dialog.findViewById<Button>(R.id.dialog_ok_button)
        btnSubmit.setOnClickListener {
            val msg = msgEt.text.toString()
            onDialogSubmit(id, msg)

        }
        dialog.show()
    }

    override fun onDialogSubmit(id: String, message: String) {
        val retrofit = Retrofit.Builder().baseUrl(Constant.BASEURL2)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
                )
            ).build()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.supporter_dialog, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
        val dialog = dialogBuilder.create()
        val tripRetrofit = retrofit.create(TripsRetrofit::class.java)
        val request = mapOf("tripId" to id, "message" to message)
        val token = sharedPreferences.getString("token", "")
        tripRetrofit.makeApology(token ?: "", request).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    // response is ssucess do you need here
                    Toast.makeText(
                        requireContext(),
                        "Message sent successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                }
//                } else if (response.code()==401) {
//                    // un authrized
////                    start login screen here
//
//                }else if(response.code() in 400..499){
//                    // you passed invalid data to api
//
//                }
//                else if(response.code()>=500){
//                    // exception in backend
//                    Toast.makeText(
//                        requireContext(),
//                        "status Code 500",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
                else{
                    var errMsg=""
                    val json=response.errorBody()!!.string()
                    val jsonObject=JSONObject(json)
                    errMsg=jsonObject.getString("msg")
                    Toast.makeText(
                        requireContext(),
                        errMsg,
                        Toast.LENGTH_SHORT
                    ).show()

                }
                val inputField = dialogView.findViewById<EditText>(R.id.dialog_message)
                inputField.text.clear()
                dialog.dismiss()

            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.e(TAG, "onFailure: ",t )
            }

        })



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

}
