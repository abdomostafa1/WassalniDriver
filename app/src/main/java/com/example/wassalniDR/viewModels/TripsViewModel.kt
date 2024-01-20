package com.example.wassalniDR.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wassalniDR.data.Driver
import com.example.wassalniDR.data.uiState.TripUiState
import com.example.wassalniDR.repo.TripRepositry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TripsViewModel(private val tripsRepositry: TripRepositry) : ViewModel() {
    private val _state = MutableStateFlow<TripUiState>(TripUiState.Loading)
    val state = _state.asStateFlow()

    private val _driverInfo = MutableStateFlow<Driver?>(null)
    val driverInfo = _driverInfo.asStateFlow()

    init {
        retrieveDriverData()
    }

    fun getTrips(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.emit(TripUiState.Loading)
                val trips = tripsRepositry.getTrips(token)
                Log.e("TAG", "viewModel Success")
                _state.value = TripUiState.Success(trips)
            } catch (ex: Exception) {
                ex.message?.let { _state.emit(TripUiState.Error(it)) }
            }
        }
    }

    private fun retrieveDriverData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val driver = tripsRepositry.retrieveDriverData()
                _driverInfo.emit(driver)
            }
            catch (error:Throwable){
                val errorMsg:String=if (error.message !=null) error.message!! else "error"
                _state.emit(TripUiState.Error(errorMsg))
            }
        }
    }
}