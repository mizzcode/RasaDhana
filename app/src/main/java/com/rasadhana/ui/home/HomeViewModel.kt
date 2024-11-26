package com.rasadhana.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rasadhana.data.pref.UserModel
import com.rasadhana.data.repository.UserRepository

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getUserData(token: String) = userRepository.getUserData(token)

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }
}