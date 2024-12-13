package com.rasadhana.ui.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rasadhana.data.local.entity.UserEntity
import com.rasadhana.data.repository.UserRepository

class SplashViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserEntity> {
        return userRepository.getSession().asLiveData()
    }
}