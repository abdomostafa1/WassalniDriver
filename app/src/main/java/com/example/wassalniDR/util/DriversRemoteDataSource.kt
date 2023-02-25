package com.example.wassalniDR.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import com.example.wassalniDR.data.Drivers
import com.example.wassalniDR.database.ApiResponse
import com.example.wassalniDR.database.FailureResponse
import com.example.wassalniDR.database.SuccessfulResponse
import com.example.wassalniDR.util.Constant.APPID
import com.example.wassalniDR.util.Constant.BASEURL
import com.example.wassalniDR.util.Constant.TAG
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DriversRemoteDataSource(val context: Context) {
    val driverCreation = MutableLiveData<ApiResponse>()



    var isOnline: Boolean? = null
    var retrofit = Retrofit.Builder()
        .baseUrl(BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var driverRetrofit: DriversRetrofit = retrofit.create(DriversRetrofit::class.java)


    @RequiresApi(Build.VERSION_CODES.N)
    fun createDriver(driver: Drivers) {
        isOnline = checkInternetConnection()
        if (!isOnline!!) {
            driverCreation.value = FailureResponse(0, null)
            return
        }


        driverRetrofit.createDriver(driver, APPID).enqueue(object : Callback<Drivers> {
            override fun onResponse(call: Call<Drivers>, response: Response<Drivers>) {

                Log.e(TAG, "response Code: ${response.code()}")
                Log.e(TAG, "response body: ${response.body().toString()}")

                if (response.isSuccessful)
                    driverCreation.value = SuccessfulResponse(response.code(), response.body()!!)
                else {
                    val errorBody: String? = response.errorBody()?.string()
                    val errorModel = parseErrorResponse(errorBody!!)
                    driverCreation.value = FailureResponse(response.code(), errorModel)

                }
            }

            override fun onFailure(call: Call<Drivers>, t: Throwable) {

                Log.e(TAG, "onFailure: ${t.message}")
                driverCreation.value = FailureResponse(0, ErrorModel(t.message!!, null))
            }
        })
    }
    private fun parseErrorResponse(errorBody: String): ErrorModel {

        val root = JSONObject(errorBody)
        val error = root.getString("error")
        var fields = HashMap<String, String>()
        if (root.has("data")) {
            val data = root.getJSONObject("data")
            data.keys().iterator().forEach {
                fields.put(it, data.getString(it))
            }
        }
        return ErrorModel(error, fields)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkInternetConnection(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}


