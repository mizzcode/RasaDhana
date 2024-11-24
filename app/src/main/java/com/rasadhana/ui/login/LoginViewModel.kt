package com.rasadhana.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {
    var numberPhone = MutableLiveData<String>()
    var password = MutableLiveData<String>()
}