package com.example.wassalniDR.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wassalniDR.data.uiState.TripUiState
import com.example.wassalniDR.repo.SupportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SupportViewModel(private val supportRepository: SupportRepository) :ViewModel(){

    private val _state = MutableStateFlow<TripUiState>(TripUiState.Loading)
    val state = _state.asStateFlow()

    fun getTrips(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.emit(TripUiState.Loading)
                val trips = supportRepository.getTrips(token)
                Log.e("TAG", "viewModel Success")
                _state.value = TripUiState.Success(trips)
            } catch (ex: Exception) {
                ex.message?.let { _state.emit(TripUiState.Error(it)) }
            }
        }
    }
}