package com.example.wassalniDR.util

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.wassalniDR.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


class Permission ( val context: Context) {
    private val dialog = MaterialAlertDialogBuilder(context)
    private var action: () -> Unit = {}

    fun isLocationPermissionEnabled(): Boolean {

        val preciseLocationPermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val approximateLocationPermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        return preciseLocationPermission == PackageManager.PERMISSION_GRANTED || approximateLocationPermission == PackageManager.PERMISSION_GRANTED
    }

    fun isGpsOpen(): Boolean {
        Log.e("checkGps 000: ", " Show me")

        val locationManager: LocationManager =
            context.getSystemService(LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        ) {
            return true
        }
        // otherwise return false
        return false

    }

    fun openGps(action:()->Unit,cancelable: Boolean) {
        this.action=action
        showDialog(cancelable)
    }

    private fun showDialog(cancelable: Boolean) {
        dialog.setIcon(R.drawable.ic_marker)
        dialog.setTitle(context.getString(R.string.turn_device_location))
        dialog.setMessage(context.getString(R.string.gps_message))
        dialog.setPositiveButton(
            context.getString(R.string.settings)){ dialog, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                gpsSettingActivity.launch(intent)
                dialog.dismiss()

            }
            .setCancelable(cancelable)

        dialog.show()
    }

    private val gpsSettingActivity = (context as AppCompatActivity).registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        action()
    }

    fun requestLocationPermission(action: () -> Unit) {
        this.action = action
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
    }

    private val locationPermissionRequest = (context as AppCompatActivity).registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {

            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                action.invoke()
                // Precise location access granted.
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION]==true -> {
                action.invoke()
                // Only approximate location access granted.
            }
            else -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.location_permission_failure),
                    Toast.LENGTH_SHORT
                ).show()
                // No location access granted.
            }
        }
    }

}