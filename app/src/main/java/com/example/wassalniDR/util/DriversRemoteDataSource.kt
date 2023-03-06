package com.example.wassalniDR.util

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import com.example.wassalniDR.R
import com.example.wassalniDR.data.Drivers
import com.example.wassalniDR.database.ApiResponse
import com.example.wassalniDR.database.FailureResponse
import com.example.wassalniDR.database.SuccessfulResponse

import com.example.wassalniDR.util.Constant.BASEURL
import com.example.wassalniDR.util.Constant.TAG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DriversRemoteDataSource(val context: Context) {
    val driverCreation = MutableLiveData<ApiResponse>()

    private lateinit var loginService:DriversRetrofit
    private lateinit var sharedPreferences: SharedPreferences

    private val _loginUiState= MutableStateFlow<LoginUiState>(LoginUiState.InitialState)
    val loginUiState: StateFlow<LoginUiState>
        get() = _loginUiState

    suspend fun makeLoginRequest(params: HashMap<String, Any>) {

        try {
            _loginUiState.emit(LoginUiState.Loading)
            val task = loginService.createDriver(params).execute()
            if (task.isSuccessful) {
                val responseParams = task.body()
                cacheUserCredential(responseParams!!)
                _loginUiState.emit(LoginUiState.LoginSuccess)
            } else {
                val body: String? = task.errorBody()?.string()
                if (body != null) {
                    handleErrorBody(body)
                }
            }

        } catch (ex: Exception) {
            handleExceptionError(ex)
        }
    }

    private fun cacheUserCredential(responseParams: Map<String, Any>) {
        val token = responseParams["token"] as String
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.putString("token", token)
        editor.apply()

    }
    private suspend fun handleErrorBody(body: String) {
        val root = JSONObject(body)
        val err = root.getJSONObject("err")
        val errors = err.getJSONObject("errors")
        errors.keys().forEach {
            val errorMsg = "$it is not correct"
            _loginUiState.emit(LoginUiState.Error(errorMsg))
        }

        Log.e(TAG, "Fail: $body")
    }

    private suspend fun handleExceptionError(ex: Exception) {
        var errorMsg: String? = null
        errorMsg = if (ex.message != null)
            ex.message
        else
            context.getString(R.string.error_occurred)

        Log.e(TAG, "catch: ${ex.message}")
        _loginUiState.emit(LoginUiState.Error(errorMsg!!))
    }

    fun resetUiState() {
        _loginUiState.value=LoginUiState.InitialState


    }










    sealed class LoginUiState(){
        object InitialState:LoginUiState()
        object Loading:LoginUiState()
        object CodeSent:LoginUiState()
        object VerificationSuccess:LoginUiState()
        object LoginSuccess:LoginUiState()
        data class Error(val errorMsg:String):LoginUiState()
    }


























//    var isOnline: Boolean? = null
//    var retrofit = Retrofit.Builder()
//        .baseUrl(BASEURL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    var driverRetrofit: DriversRetrofit = retrofit.create(DriversRetrofit::class.java)
//
//
//    @RequiresApi(Build.VERSION_CODES.N)
//    fun createDriver(driver: Drivers) {
//        isOnline = checkInternetConnection()
//        if (!isOnline!!) {
//            driverCreation.value = FailureResponse(0, null)
//            return
//        }
//
//
//        driverRetrofit.createDriver(driver).enqueue(object : Callback<Drivers> {
//            override fun onResponse(call: Call<Drivers>, response: Response<Drivers>) {
//
//                Log.e(TAG, "response Code: ${response.code()}")
//                Log.e(TAG, "response body: ${response.body().toString()}")
//
//                if (response.isSuccessful)
//                    driverCreation.value = SuccessfulResponse(response.code(), response.body()!!)
//                else {
//                    val errorBody: String? = response.errorBody()?.string()
//                    val errorModel = parseErrorResponse(errorBody!!)
//                    driverCreation.value = FailureResponse(response.code(), errorModel)
//
//                }
//            }
//
//            override fun onFailure(call: Call<Drivers>, t: Throwable) {
//
//                Log.e(TAG, "onFailure: ${t.message}")
//                driverCreation.value = FailureResponse(0, ErrorModel(t.message!!, null))
//            }
//        })
//    }
//    private fun parseErrorResponse(errorBody: String): ErrorModel {
//
//        val root = JSONObject(errorBody)
//        val error = root.getString("error")
//        var fields = HashMap<String, String>()
//        if (root.has("data")) {
//            val data = root.getJSONObject("data")
//            data.keys().iterator().forEach {
//                fields.put(it, data.getString(it))
//            }
//        }
//        return ErrorModel(error, fields)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun checkInternetConnection(): Boolean {
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        if (connectivityManager != null) {
//            val capabilities =
//                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
//            if (capabilities != null) {
//                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
//                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
//                    return true
//                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
//                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
//                    return true
//                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
//                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
//                    return true
//                }
//            }
//        }
//        return false
//    }
}


