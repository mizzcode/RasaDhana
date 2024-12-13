package com.rasadhana.ui.setting

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rasadhana.data.local.entity.UserEntity
import com.rasadhana.data.repository.UserRepository
import kotlinx.coroutines.launch
import java.io.File

class SettingViewModel(private val userRepository: UserRepository) : ViewModel() {
    var currentImageUri: Uri? = null

    fun saveSession(user: UserEntity) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun updateUserSetting(photo: File?, name: String?, userId: String) = userRepository.updateUserSetting(photo, name, userId)
}