package com.rasadhana.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rasadhana.data.pref.UserModel
import com.rasadhana.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository): ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun getUserData(token: String) = userRepository.getUserData(token)

    fun login(email: String, password: String) = userRepository.login(email, password)
}