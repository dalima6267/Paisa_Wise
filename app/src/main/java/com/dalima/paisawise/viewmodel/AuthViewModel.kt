package com.dalima.paisawise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dalima.paisawise.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _authStatus = MutableLiveData<Result<Unit>?>()
    val authStatus: LiveData<Result<Unit>?> = _authStatus

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authStatus.postValue(repository.signUpWithEmail(name, email, password))
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authStatus.postValue(repository.signInWithEmail(email, password))
        }
    }

    fun signInWithGoogle(credential: AuthCredential) {
        viewModelScope.launch {
            _authStatus.postValue(repository.signInWithGoogle(credential))
        }
    }

    fun clearStatus() {
        _authStatus.postValue(null)
    }
}