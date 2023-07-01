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
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "TripDetailsViewModel"

@HiltViewModel
class TripDetailsViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val repo: TripDetailsRepository
) : ViewModel() {
    private lateinit var tripDetails: TripDetails

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

    private val _tripFareUiState = MutableStateFlow<TripFareUiState>(TripFareUiState.InitialState)
    val tripFareUiState = _tripFareUiState.asStateFlow()

    fun getTripDetails(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _tripDetailsUiState.emit(TripUiState.Loading)
            tripDetails = repo.getTripDetails(id)
            _tripDetailsUiState.emit(TripUiState.Success(listOf()))
            getPolyLine()
        }
    }

    fun didTripStart(): Boolean {
        return tripDetails.stations[0].isArrived
    }

    fun getTripPrice(): Double {
        return tripDetails.price
    }

    private fun calculateNextStation() {
        val stations = tripDetails.stations
        for (i in stations.indices) {
            if (!stations[i].isArrived) {
                Log.e(TAG, "nextStationIndex =$i ")
                nextStationIndex = i
                _onNewStation.value = stations[i]
                break
            }
        }
    }

    fun getNextStationLocation(): LatLng {
        val location = tripDetails.stations[nextStationIndex].location
        return LatLng(location.lat, location.lng)

    }

    fun confirmArrival(driverLocation: LatLng) {
//        if (!canConfirmArrival(driverLocation))
//            return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _confirmArrivalState.postValue(ConfirmArrivalUiState.Loading)
                val response = repo.confirmArrival(tripDetails._id, nextStationIndex)
                tripDetails.passengers = response.passengers
                val station = tripDetails.stations[nextStationIndex]
                station.isArrived = true
                _confirmArrivalState.postValue(ConfirmArrivalUiState.Success)
                showToastFromBackgroundThread(context.getString(R.string.arrival_confirmed))
            } catch (e: Exception) {
                e.printStackTrace()
                _confirmArrivalState.postValue(ConfirmArrivalUiState.Error)
                if (e.message != null) {
                    showToastFromBackgroundThread(e.message!!)
                }
            }
        }
    }

    private fun canConfirmArrival(driverLocation: LatLng): Boolean {
        return isValidTime() && isValidDistance(driverLocation)
    }

    private fun isValidTime(): Boolean {
        val currentTime = System.currentTimeMillis() / 1000
        val stationArrivalTime = tripDetails.stations[nextStationIndex].time
        if ((stationArrivalTime - currentTime) <= (10 * 60))
            return true
        else {
            _confirmArrivalState.postValue(ConfirmArrivalUiState.Error)
            showToastMessage(context.getString(R.string.confirm_arrival_before_ten_minutes))
        }
        return false
    }

    private fun isValidDistance(driverLocation: LatLng): Boolean {
        val location = tripDetails.stations[nextStationIndex].location
        val nextStationLocation = LatLng(location.lat, location.lng)
        // distance in metres
        val distance = repo.calculateDistance(driverLocation, nextStationLocation)
        if (distance <= 100)
            return true
        else {
            _confirmArrivalState.postValue(ConfirmArrivalUiState.Error)
            showToastMessage(context.getString(R.string.not_reached_station))
        }
        return false
    }

    fun getPassengersOfCurrentStation(): List<Passenger> {
        val passengers = tripDetails.passengers
        Log.e(TAG, "passengers =$passengers ")
        val stationsSize = tripDetails.stations.size.minus(1)
        val currentStation = if (nextStationIndex == stationsSize)
            nextStationIndex
        else
            nextStationIndex - 1

        Log.e(TAG, "currentStation =$currentStation ")
        val passengersOfCurrentStation = passengers.filter {
            it.point <= currentStation && it.hasCome == 0
        }
        Log.e(TAG, "passengersOfCurrentStation=$passengersOfCurrentStation ")
        return passengersOfCurrentStation

    }

    fun getPassengersOfPastStations(): List<Passenger> {
        val passengers = tripDetails.passengers
        val currentStation = nextStationIndex - 1
        val passengersOfCurrentStation = passengers.filter {
            it.point <= currentStation && it.hasCome == 1 && !it.hasArrived
        }
        Log.e("TAG", "currentStation:$currentStation ")
        Log.e("TAG", "$passengersOfCurrentStation ")
        return passengersOfCurrentStation
    }

    fun recordPassengerAttendance(passengerId: String) {
        val passenger = tripDetails.passengers[getPassengerIndex(passengerId)]
        passenger.hasCome = 1
        viewModelScope.launch(Dispatchers.IO) {
            repo.recordPassengerAttendance(tripDetails._id, passenger.ticket)
        }
    }

    fun recordPassengerAbsence(passengerId: String) {
        val passenger = tripDetails.passengers[getPassengerIndex(passengerId)]
        passenger.hasCome = -1
        viewModelScope.launch(Dispatchers.IO) {
            repo.recordPassengerAbsence(tripDetails._id, passenger.ticket)
        }
    }

    fun recordPassengerArrival(passengerId: String) {
        val passenger = tripDetails.passengers[getPassengerIndex(passengerId)]
        passenger.hasArrived = true
        updateTripLifecycleState()
        viewModelScope.launch(Dispatchers.IO) {
            repo.recordPassengerArrival(tripDetails._id, passenger.ticket)
        }
    }

    private fun getPassengerIndex(passengerId: String): Int {
        var index = 0
        val passengers = tripDetails.passengers
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
        for (passenger in tripDetails.passengers) {
            if (passenger.hasCome >= 0 && !passenger.hasArrived) {
                result = true
                break
            }
        }
        return result
    }

    private fun hasArrivedLastStation(): Boolean {
        var result = true
        for (station in tripDetails.stations) {
            if (!station.isArrived) {
                result = false
                break
            }
        }
        return result
    }

    fun updateTripLifecycleState() {
        Log.e(TAG, "updateTripLifecycleState: ")
        if (!isTherePassengers() && hasArrivedLastStation()) {
            Log.e(TAG, "_tripFinishState")
            _tripFinishState.value = true
        } else if (hasArrivedLastStation()) {
            Log.e(TAG, "calculateNextStation")
            _lastStationState.value = true
        } else {
            Log.e(TAG, "calculateNextStation")
            calculateNextStation()
        }
    }

    private fun getPolyLine() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _polyline.value = repo.getPolyLine(tripDetails.stations)
            } catch (ex: Exception) {
                Log.e("TAG", "getPolyLine1: ${ex.message}")
            }
        }
    }

    fun getAttendPassengers(): List<Passenger> {
        val passengers = tripDetails.passengers.filter {
            it.hasCome == 1
        }
       return passengers
    }

    fun calculateTotal(): Double {
        var total = 0.0
        val tripPrice = getTripPrice()
        val passengers = getAttendPassengers()
        for (passenger in passengers) {
            val fare = passenger.numOfSeat * tripPrice
            total += fare
        }
        return total
    }

    fun endTrip() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _tripFareUiState.value = TripFareUiState.Loading
                repo.endTrip(tripDetails._id)
                _tripFareUiState.value = TripFareUiState.Success
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    _tripFareUiState.value = TripFareUiState.Error
                }
            }
        }
    }


    private suspend fun showToastFromBackgroundThread(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun determineNextStationIndex() {
        val stations = tripDetails.stations
        for (i in stations.indices) {
            if (stations[i].isArrived)
                nextStationIndex = i
        }
    }

    fun getCurrentStation(): String {
        if (nextStationIndex != 0) {
            val station = tripDetails.stations[nextStationIndex - 1]
            return station.name
        } else
            return ""
    }

    fun setLastStationState(value:Boolean) {
        _lastStationState.value=value
    }
    fun setTripFinishState(value:Boolean) {
        _tripFinishState.value=value
    }

}


