package com.example.wassalniDR.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.wassalniDR.data.uiState.TripUiState
import com.example.wassalniDR.repo.RatingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.example.wassalniDR.data.uiState.RatingUiState

class RatingViewModel(private val ratingRepository: RatingRepository):ViewModel() {
    private val _state = MutableStateFlow<RatingUiState>(RatingUiState.Loading)
    val state = _state.asStateFlow()

    fun getRating(token:String)
    {
       viewModelScope.launch ( Dispatchers.IO ){
           try {
               _state.emit(RatingUiState.Loading)
               val rating = ratingRepository.getRating(token)
               Log.e("TAG", "viewModel Success")
               _state.value = RatingUiState.Success(rating)
           } catch (ex: Exception) {
               ex.message?.let { _state.emit(RatingUiState.Error(it)) }
           }
       }
    }
}