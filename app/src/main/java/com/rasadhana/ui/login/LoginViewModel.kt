package com.rasadhana.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rasadhana.data.local.entity.UserEntity
import com.rasadhana.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository): ViewModel() {
    fun saveSession(user: UserEntity) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun getSession(): LiveData<UserEntity> {
        return userRepository.getSession().asLiveData()
    }

    fun getUserData(token: String) = userRepository.getUserData(token)

    fun login(email: String, password: String) = userRepository.login(email, password)
}