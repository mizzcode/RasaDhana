package com.rasadhana.ui.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IngredientViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Ingredient Fragment"
    }
    val text: LiveData<String> = _text
}