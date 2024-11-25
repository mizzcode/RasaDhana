package com.rasadhana.ui.forgotpassword

import androidx.lifecycle.ViewModel
import com.rasadhana.data.repository.UserRepository

class CreateNewPasswordViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun updatePassword(otp: String, newPassword: String) = userRepository.updatePassword(otp, newPassword)
}