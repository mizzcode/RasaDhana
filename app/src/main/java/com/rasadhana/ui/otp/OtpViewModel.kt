package com.rasadhana.ui.otp

import androidx.lifecycle.ViewModel
import com.rasadhana.data.repository.UserRepository

class OtpViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun userVerify(email: String, otp: String) = userRepository.registerVerify(email, otp)
}