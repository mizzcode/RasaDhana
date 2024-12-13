package com.rasadhana.ui.account

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rasadhana.data.local.entity.UserEntity
import com.rasadhana.data.repository.UserRepository
import kotlinx.coroutines.launch

class AccountViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserEntity> {
        return userRepository.getSession().asLiveData()
    }

    fun logout(context: Context) {
        viewModelScope.launch {
            try {
                userRepository.logout()
                Toast.makeText(context, "Logout berhasil", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}