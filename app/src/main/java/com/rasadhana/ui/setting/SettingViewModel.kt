package com.rasadhana.ui.setting

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.rasadhana.data.repository.UserRepository
import java.io.File

class SettingViewModel(private val userRepository: UserRepository) : ViewModel() {
    var currentImageUri: Uri? = null

    fun updateUser(photo: File, name: String, userId: String) = userRepository.updateUser(photo, name, userId)
}