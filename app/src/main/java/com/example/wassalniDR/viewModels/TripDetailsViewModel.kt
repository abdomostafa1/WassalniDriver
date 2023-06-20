package com.example.wassalniDR.viewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wassalniDR.R
import com.example.wassalniDR.data.Passenger
import com.example.wassalniDR.data.Station
import com.example.wassalniDR.data.TripDetails
import com.example.wassalniDR.data.uiState.ConfirmArrivalUiState
import com.example.wassalniDR.data.uiState.TripFareUiState
import com.example.wassalniDR.data.uiState.TripUiState
import com.example.wassalniDR.repo.TripDetailsRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripDetailsViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val repo: TripDetailsRepository
) : ViewModel() {
    private var tripDetails: TripDetails? = null

    private val _tripDetailsUiState = MutableStateFlow<TripUiState>(TripUiState.Loading)
    val tripDetailsUiState = _tripDetailsUiState.asStateFlow()

    private val _polyline = MutableStateFlow<List<LatLng>?>(null)
    val polyline = _polyline.asStateFlow()

    private val _confirmArrivalState =
        MutableLiveData<ConfirmArrivalUiState>(ConfirmArrivalUiState.InitialState)
    val confirmArrivalState: LiveData<ConfirmArrivalUiState>
        get() = _confirmArrivalState

    private val _tripFinishState = MutableStateFlow(false)
    val tripFinishState = _tripFinishState.asStateFlow()

    private val _lastStationState = MutableStateFlow(false)
    val lastStationState = _lastStationState.asStateFlow()

    private var nextStationIndex = 0

    private val _onNewStation = MutableStateFlow<Station?>(null)
    val onNewStation = _onNewStation.asStateFlow()

    private val _tripFareUiState= MutableStateFlow<TripFareUiState>(TripFareUiState.InitialState)
    val tripFareUiState= _tripFareUiState.asStateFlow()

    fun getTripDetails(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _tripDetailsUiState.emit(TripUiState.Loading)
            tripDetails = repo.getTripDetails(id)
            _tripDetailsUiState.emit(TripUiState.Success(listOf()))
            getPolyLine()
        }
    }

    fun didTripStart(): Boolean {
        return tripDetails?.stations?.get(0)?.isArrived == true
    }

    fun getTripPrice(): Double {
        val price=tripDetails?.price
        return if (price!=null)
            price
        else
            0.0
    }

    private fun calculateNextStation() {
        val stations = tripDetails?.stations!!
        for (i in 0 until stations.size - 1) {
            if (!stations[i].isArrived) {
                nextStationIndex = i
                _onNewStation.value = stations[i]
                break
            }
        }
    }

    fun getNextStationLocation(): LatLng {
        val location = tripDetails?.stations?.get(nextStationIndex)?.location!!
        return LatLng(location.lat, location.lng)

    }

    fun confirmArrival(driverLocation: LatLng) {
//        if (!canConfirmArrival(driverLocation))
//            return

        val stationIndex = nextStationIndex
        val station = tripDetails?.stations?.get(stationIndex)
        station?.isArrived = true
        updateTripLifecycleState()

        viewModelScope.launch(Dispatchers.IO) {
            _confirmArrivalState.value = ConfirmArrivalUiState.Loading
            repo.confirmArrival(tripDetails?._id!!, stationIndex)
            _confirmArrivalState.value = ConfirmArrivalUiState.Success
        }
    }

    private fun canConfirmArrival(driverLocation: LatLng): Boolean {
        return isValidTime() && isValidDistance(driverLocation)

    }

    private fun isValidTime(): Boolean {
        val currentTime = System.currentTimeMillis() / 1000
        val stationArrivalTime = tripDetails?.stations?.get(nextStationIndex)?.time
        if (stationArrivalTime != null) {
            // confirm arrival is allowed before 10 minutes of station arrival
            if ((stationArrivalTime - currentTime) <= (10 * 60))
                return true
            else
                _confirmArrivalState.value =
                    ConfirmArrivalUiState.Error(context.getString(R.string.confirm_arrival_before_ten_minutes))
        }
        return false
    }

    private fun isValidDistance(driverLocation: LatLng): Boolean {
        val location = tripDetails?.stations?.get(nextStationIndex)?.location
        val nextStationLocation = LatLng(location?.lat!!, location.lng)
        // distance in metres
        val distance = repo.calculateDistance(driverLocation, nextStationLocation)
        if (distance <= 100)
            return true
        else
            _confirmArrivalState.value =
                ConfirmArrivalUiState.Error(context.getString(R.string.not_reached_station))
        return false
    }

    fun getPassengersOfCurrentStation(): List<Passenger> {
        val passengers = tripDetails?.passengers!!
        val currentStation = nextStationIndex - 1
        val passengersOfCurrentStation = passengers.filter {
            it.point == currentStation && it.hasCome == 0
        }
        return passengersOfCurrentStation
    }

    fun getPassengersOfPastStations(): List<Passenger> {
        val passengers = tripDetails?.passengers!!
        val currentStation = nextStationIndex - 1
        val passengersOfCurrentStation = passengers.filter {
            it.point <= currentStation && it.hasCome == 1 && !it.hasArrived
        }
        Log.e("TAG", "currentStation:$currentStation ")
        Log.e("TAG", "$passengersOfCurrentStation ")
        return passengersOfCurrentStation
    }

    fun recordPassengerAttendance(passengerId: String) {
        val passenger = tripDetails?.passengers!![getPassengerIndex(passengerId)]
        passenger.hasCome = 1
        viewModelScope.launch(Dispatchers.IO) {
            repo.recordPassengerAttendance(tripDetails?._id!!, passenger.ticket)
        }
    }

    fun recordPassengerAbsence(passengerId: String) {
        val passenger = tripDetails?.passengers!![getPassengerIndex(passengerId)]
        passenger.hasCome = -1
        viewModelScope.launch(Dispatchers.IO) {
            repo.recordPassengerAbsence(tripDetails?._id!!, passenger.ticket)
        }
    }

    fun recordPassengerArrival(passengerId: String) {
        val passenger = tripDetails?.passengers!![getPassengerIndex(passengerId)]
        passenger.hasArrived = true
        updateTripLifecycleState()
        viewModelScope.launch(Dispatchers.IO) {
            repo.recordPassengerArrival(tripDetails?._id!!, passenger.ticket)
        }
    }

    private fun getPassengerIndex(passengerId: String): Int {
        var index = 0
        val passengers = tripDetails?.passengers!!
        for (i in passengers.indices) {
            if (passengers[i].id == passengerId) {
                index = i
                break
            }
        }
        return index
    }

    private fun isTherePassengers(): Boolean {
        var result = false
        for (passenger in tripDetails?.passengers!!) {
            if (passenger.hasCome >= 0 && !passenger.hasArrived) {
                result = true
                break
            }

        }
        return result
    }

    private fun hasArrivedLastStation(): Boolean {
        var result = true
        for (station in tripDetails?.stations!!) {
            if (!station.isArrived) {
                result = false
                break
            }
        }
        return result
    }

    fun updateTripLifecycleState() {
        if (!isTherePassengers() && hasArrivedLastStation())
            _tripFinishState.value = true
        else if (hasArrivedLastStation())
            _lastStationState.value = true
        else
            calculateNextStation()
    }

    private fun getPolyLine() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _polyline.value = repo.getPolyLine(tripDetails?.stations!!)
            } catch (ex: Exception) {
                Log.e("TAG", "getPolyLine1: ${ex.message}")
            }
        }
    }

    fun getAttendPassengers() :List<Passenger>{
        val passengers = tripDetails?.passengers?.filter {
            it.hasCome == 1
        }
        return if (passengers!=null)
            passengers
        else
            emptyList()

    }

    fun calculateTotal():Double {
        var total=0.0
        val tripPrice=getTripPrice()
        val passengers=getAttendPassengers()
        for (passenger in passengers){
            val fare=passenger.numOfSeat * tripPrice
            total+=fare
        }
        return total
    }
    fun finishTrip() {
        viewModelScope.launch (Dispatchers.IO){
            try {
                _tripFareUiState.value=TripFareUiState.Loading
                repo.finishTrip()
                _tripFareUiState.value=TripFareUiState.Success
            }
            catch (e:Exception){
                e.printStackTrace()
                Toast.makeText(context,e.message,Toast.LENGTH_LONG).show()
                _tripFareUiState.value=TripFareUiState.Error
            }
        }
    }

}