package com.rasadhana.ui.setting

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rasadhana.data.pref.UserModel
import com.rasadhana.data.repository.UserRepository
import kotlinx.coroutines.launch
import java.io.File

class SettingViewModel(private val userRepository: UserRepository) : ViewModel() {
    var currentImageUri: Uri? = null

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun updateUser(photo: File?, name: String?, userId: String) = userRepository.updateUser(photo, name, userId)
}