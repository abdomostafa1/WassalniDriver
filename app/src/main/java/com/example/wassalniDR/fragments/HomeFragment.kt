package com.example.wassalniDR.fragments

import android.R
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.wassalniDR.databinding.FragmentHomeBinding
import com.example.wassalniDR.util.Constant.REQUEST_CODE
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class HomeFragment:Fragment(),OnMapReadyCallback,OnMarkerClickListener {

    lateinit var binding: FragmentHomeBinding
    lateinit var gMap: GoogleMap
    lateinit var location: Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding= FragmentHomeBinding.inflate(inflater)
//        val mapFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(requireActivity())
//        return binding.root
//    }
    override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    val rootView: View = inflater.inflate(com.example.wassalniDR.R.layout.fragment_home, container, false)
    val mapFragment = childFragmentManager.findFragmentById(com.example.wassalniDR.R.id.map) as SupportMapFragment?
    mapFragment!!.getMapAsync(this)
    fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(requireActivity())
    return rootView
}
    override fun onMapReady(p0: GoogleMap) {
        this.gMap = p0
        gMap.uiSettings.isZoomControlsEnabled=true
        gMap.setOnMarkerClickListener(this)
        getLocation()
    }
    private fun getLocation() {
        if(ActivityCompat.checkSelfPermission(
                requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(requireActivity(),arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE)
            return
        }
        gMap.isMyLocationEnabled=true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if(it!=null)
            {
                location=it
                val currentLog= LatLng(it.latitude,it.longitude)
                placeMarkeronMap(currentLog)
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLog,10f))
            }
        }
    }
    private fun placeMarkeronMap(currentLog: LatLng) {
        val markerOption= MarkerOptions().position(currentLog)
        markerOption.title("$currentLog")
        gMap.addMarker(markerOption)

    }

    override fun onMarkerClick(p0: Marker)=false
}