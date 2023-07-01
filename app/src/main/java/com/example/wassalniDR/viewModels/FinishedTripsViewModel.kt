package com.example.wassalniDR.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wassalniDR.data.uiState.TripUiState
import com.example.wassalniDR.repo.FinishedTripRepository
import com.example.wassalniDR.repo.TripRepositry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FinishedTripsViewModel(private val finishedTripRepository: FinishedTripRepository ) : ViewModel() {
    private val _state = MutableStateFlow<TripUiState>(TripUiState.Loading)
    val state = _state.asStateFlow()

    fun getPerviousTrips(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.emit(TripUiState.Loading)
                val trips = finishedTripRepository.getPerviousTrips(token)
                Log.e("TAG", "viewModel Success")
                _state.value = TripUiState.Success(trips)
            } catch (ex: Exception) {
                ex.message?.let { _state.emit(TripUiState.Error(it)) }
            }
        }
    }
}