package com.rasadhana.ui.forgotpassword

import androidx.lifecycle.ViewModel
import com.rasadhana.data.repository.UserRepository

class ForgotPasswordViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getOtp(email: String) = userRepository.getOtp(email)
}