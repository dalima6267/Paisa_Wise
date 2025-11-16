package com.dalima.paisawise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dalima.paisawise.data.User
import com.dalima.paisawise.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.launch



class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _authStatus = MutableLiveData<Result<Unit>?>()
    val authStatus: LiveData<Result<Unit>?> = _authStatus

    private val _userProfile = MutableLiveData<User?>()
    val userProfile: LiveData<User?> = _userProfile

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
    fun fetchUserProfile() {
        val currentUser = repository.getCurrentUser() ?: return

        repository.listenToUserProfile(currentUser.uid) { user ->
            _userProfile.postValue(user)
        }
    }


    fun signInWithGoogle(credential: AuthCredential) {
        viewModelScope.launch {
            _authStatus.postValue(repository.signInWithGoogle(credential))
        }
    }
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        // For example, password must be at least 6 chars
        return password.length >= 6
    }

    fun clearStatus() {
        _authStatus.postValue(null)
    }
}