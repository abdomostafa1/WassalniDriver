package com.example.wassalniDR.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.wassalniDR.R
import com.google.android.gms.location.*

class LocationService : Service() {

    companion object {
        var driverLocation: Location? = null
    }

    private lateinit var fusedLocation: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    lateinit var locationRequest: LocationRequest
    lateinit var notificationManager: NotificationManager
    private val channelId = "location_channel"
    private val notificationId = 12345678
    override fun onCreate() {
        super.onCreate()

        notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Location Service Updates", NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = createLocationCallback()
        locationRequest = LocationRequest.Builder(10000L)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()
    }

    override fun onBind(intent: Intent): IBinder? = null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return START_STICKY
        }
        fusedLocation.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

        startForeground(notificationId,getNotification().build())
        return super.onStartCommand(intent, flags, startId)
    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
    }

    private fun createLocationCallback(): LocationCallback {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                driverLocation = p0.lastLocation
                val notificationBuilder = getNotification()
                val updatedNotification=notificationBuilder.
                setContentText("latitude:${driverLocation?.latitude},longitude:${driverLocation?.longitude}")
                    .build()
                startForeground(notificationId,updatedNotification)
            }
        }
        return locationCallback
    }

    fun getNotification(): NotificationCompat.Builder {

        return NotificationCompat.Builder(this, channelId).setContentTitle("your Location ")
            .setContentText("").setPriority(Notification.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.ic_marker)
    }


}