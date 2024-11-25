package com.rasadhana.ui.register

import androidx.lifecycle.ViewModel
import com.rasadhana.data.repository.UserRepository

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun register(name: String,
                 email: String,
                 password: String)
    = userRepository.register(name,email,password)
}