package com.rasadhana.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rasadhana.data.local.entity.UserEntity
import com.rasadhana.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserEntity> {
        return userRepository.getSession().asLiveData()
    }
}