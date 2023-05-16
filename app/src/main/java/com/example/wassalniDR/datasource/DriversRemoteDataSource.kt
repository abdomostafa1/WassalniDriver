package com.example.wassalniDR.util

import android.content.SharedPreferences
import android.util.Log
import com.example.wassalniDR.database.DriversRetrofit

import javax.inject.Inject


class DriversRemoteDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val loginService: DriversRetrofit) {

    suspend fun makeLoginRequest(params: HashMap<String, Any>) {

            val task = loginService.loginDriver(params).execute()
            if (task.isSuccessful) {
                val responseParams = task.body()
                val email=params["email"] as String
                cacheUserCredential(responseParams!!,email)
            } else
                throw Exception( task.errorBody()?.string())

        }

    private fun cacheUserCredential(responseParams: Map<String, Any>, email: String) {

        val token = responseParams["token"] as String
        Log.e("token",token)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.putString("token", token)
        editor.putString("email", email)
        editor.apply()
    }
}



    sealed class LoginUiState(){
        object InitialState:LoginUiState()
        object Loading:LoginUiState()

        object LoginSuccess:LoginUiState()
        object Error:LoginUiState()
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



