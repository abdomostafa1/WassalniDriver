package com.example.wassalniDR.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wassalniDR.data.TripDetails
import com.example.wassalniDR.data.uiState.TripUiState
import com.example.wassalniDR.repo.TripDetailsRepositry
import com.example.wassalniDR.util.DateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TripDetailsViewModel(private val tripsDetailsRepositry: TripDetailsRepositry):ViewModel()
{
    private var tripDetails: TripDetails? = null
    private val _state = MutableStateFlow<TripUiState?>(null)
    val state = _state.asStateFlow()

    fun getTripDetails(id:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            tripDetails=tripsDetailsRepositry.getTripDetails(id)
            val startDestnation= tripDetails?.start!!
            val endDestnations=tripDetails?.destination!!
            val startTime=DateUseCase.fromMillisToString1(tripDetails!!.startTime)
            val endTime=DateUseCase.fromMillisToString1(tripDetails!!.endTime)
        }
    }
}