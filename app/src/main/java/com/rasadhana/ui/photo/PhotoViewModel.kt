package com.rasadhana.ui.photo

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rasadhana.data.local.entity.UserEntity
import com.rasadhana.data.repository.RecipeRepository
import com.rasadhana.data.repository.UploadRepository
import com.rasadhana.data.repository.UserRepository
import java.io.File

class PhotoViewModel(
    private val uploadRepository: UploadRepository,
    private val userRepository: UserRepository,
    private val recipeRepository: RecipeRepository,
) : ViewModel()
{
    var currentImageUri: Uri? = null

    fun uploadImage(file: File, userId: String) = uploadRepository.uploadImage(file, userId)

    fun generateRecipe(userId: String, context: Context) = recipeRepository.generateRecipe(userId, context)

    fun getSession(): LiveData<UserEntity> {
        return userRepository.getSession().asLiveData()
    }
}