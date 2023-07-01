package com.example.wassalniDR.data.uiState

import com.example.wassalniDR.data.Rating


sealed class RatingUiState {
    object Loading:RatingUiState()

    data class Success(val rating: List<Rating>) : RatingUiState()
    data class Error(val errorMsg: String) : RatingUiState()
    object Empty:RatingUiState()
}