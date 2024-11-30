package com.rasadhana.ui.photo

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rasadhana.data.pref.UserModel
import com.rasadhana.data.repository.UploadRepository
import com.rasadhana.data.repository.UserRepository
import java.io.File

class PhotoViewModel(private val uploadRepository: UploadRepository, private val userRepository: UserRepository) : ViewModel() {
    var currentImageUri: Uri? = null

    fun uploadImage(file: File, userId: String) = uploadRepository.uploadImage(file, userId)

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }
}