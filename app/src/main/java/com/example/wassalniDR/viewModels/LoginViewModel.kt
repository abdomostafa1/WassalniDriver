package com.example.wassalniDR.viewModels

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wassalniDR.R
import com.example.wassalniDR.repo.LoginRepository
import com.example.wassalniDR.util.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository,@ApplicationContext private val  context: Context):ViewModel() {

    // Register Fragment LiveData
    var password: String? = null
    var email: String? = null

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.InitialState)
    val loginUiState= _loginUiState.asStateFlow()

     fun makeLoginRequest(email:String,password:String) {
        if (!canLogin(email, password))
            return
        viewModelScope.launch (Dispatchers.IO){
            try {
                _loginUiState.value = LoginUiState.Loading
                loginRepository.makeLoginRequest(email, password)
                _loginUiState.value = LoginUiState.LoginSuccess
            }
            catch (ex:Exception){
                _loginUiState.value=LoginUiState.Error
            }
        }
    }

    private fun canLogin(email:String, password:String):Boolean{
        if (!isEmailValid(email)) {
            Toast.makeText(context, context.getString(R.string.email_invalid), Toast.LENGTH_LONG).show()
            return false
        }
        if (!isValidPassword(password)){
            Toast.makeText(context, context.getString(R.string.passsword_invalid), Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun isEmailValid(email: String):Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >=6
    }

}