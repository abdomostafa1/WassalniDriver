package com.example.wassalniDR.viewModels

import androidx.lifecycle.ViewModel
import com.example.wassalniDR.repo.LoginRepositry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel(private val loginRepositry: LoginRepositry):ViewModel() {

    // Register Fragment LiveData
    var password: String? = null
    var email: String? = null

    private val _emailError = MutableStateFlow<Int>(0)
    val emailError: StateFlow<Int>
        get() = _emailError

    private val _passwordError = MutableStateFlow<Int>(0)
    val passwordError: StateFlow<Int>
        get() = _passwordError

    val loginUiState = loginRepositry.loginUiState

    suspend fun makeLoginRequest() {
        loginRepositry.makeLoginRequest(email!!, password!!)
    }

    fun resetUiState() {
        _emailError.value=0
        _passwordError.value=0
        loginRepositry.resetUiState()
    }
}