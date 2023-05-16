package com.example.wassalniDR.fragments.tripGraph

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.wassalniDR.R
import com.example.wassalniDR.data.uiState.TripUiState
import com.example.wassalniDR.databinding.FragmentTripDetailsBinding
import com.example.wassalniDR.util.Permission
import com.example.wassalniDR.viewModels.TripDetailsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TripDetailsFragment : Fragment() , OnMapReadyCallback {
    private lateinit var binding: FragmentTripDetailsBinding
    private val viewModel: TripDetailsViewModel by navGraphViewModels(R.id.trip_graph) { defaultViewModelProviderFactory }
    private val args:TripDetailsFragmentArgs by navArgs()
    lateinit var mapFragment: SupportMapFragment
    private lateinit var map: GoogleMap

    @Inject
    lateinit var permission: Permission
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTripDetailsBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = args.id
        Log.e("TAG", "args.id:$id ", )
        viewModel.getTripDetails(id)

        mapFragment = childFragmentManager
            .findFragmentById(R.id.tripMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.openTrip.setOnClickListener {
            openTripFragment()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tripDetailsUiState.collect { state ->
                    when (state) {
                        is TripUiState.Loading -> {
                            showLoadingState()
                        }
                        is TripUiState.Success -> {
                           showSuccessState()
                        }
                        is TripUiState.Error -> {
                            showErrorState(state.errorMsg)
                        }
                        else -> Unit
                    }
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.polyline.collect { points ->
                    if (points != null) {
                        Log.e("TAG", "new polyline1: ")
                        val polyline = PolylineOptions().addAll(points!!)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            polyline.color(requireActivity().getColor(R.color.violet))
                        }
                        polyline.width(10f)
                        map.addPolyline(polyline)
                        drawMarker(points[0], points[points.size - 1])
                    }
                }
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        val mapStyleOptions =
            MapStyleOptions.loadRawResourceStyle(requireActivity(), R.raw.silver_map_style)
        map.setMapStyle(mapStyleOptions)
    }
    private fun openTripFragment() {
        Log.e("TAG", "openTripFragment: called")
        if (ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permission.requestLocationPermission(::openTripFragment)
            return
        }

        if (!permission.isGpsOpen()) {
            permission.openGps(::openTripFragment, false)
            return
        }

        findNavController().navigate(R.id.action_tripDetailsFragment_to_rideLifecycleFragment)
    }

    private fun drawMarker(point1: LatLng, point2: LatLng) {
        val redRecordMarker=bitmapDescriptorFromVector(requireActivity(),R.drawable.red_record)
        val blueRecordMarker=bitmapDescriptorFromVector(requireActivity(),R.drawable.blue_record)

        map.addMarker(MarkerOptions().position(point1).icon(redRecordMarker))
        map.addMarker(MarkerOptions().position(point2).icon(blueRecordMarker))
        setMapBounds(point1, point2)
    }

    private fun setMapBounds(point1: LatLng, point2: LatLng) {

        val north = maxOf(point1.latitude, point2.latitude)
        val east = maxOf(point1.longitude, point2.longitude)
        val south = minOf(point1.latitude, point2.latitude)
        val west = minOf(point1.longitude, point2.longitude)
        val bounds = LatLngBounds(
            LatLng(south, west),
            LatLng(north, east)
        )
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(bounds.center, 9.5f))
        //map1.animate
        // map2.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 60))

    }

    private fun bitmapDescriptorFromVector(
        context: Context,
        vectorResId: Int
    ): BitmapDescriptor? {

        // retrieve the actual drawable
        val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val bm = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // draw it onto the bitmap
        val canvas = android.graphics.Canvas(bm)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bm)
    }

    private fun showLoadingState() {
        binding.loader.visibility = View.VISIBLE
    }

    private fun showSuccessState() {
        binding.loader.visibility = View.INVISIBLE
        binding.openTrip.visibility = View.VISIBLE
        if (viewModel.didTripStart())
            binding.openTrip.text = getString(R.string.continue_trip)
        else
            binding.openTrip.text = getString(R.string.start_trip)
    }

    private fun showErrorState(errorMsg:String) {
        binding.loader.visibility = View.INVISIBLE
        Toast.makeText(requireActivity(),errorMsg,Toast.LENGTH_LONG).show()
    }
}