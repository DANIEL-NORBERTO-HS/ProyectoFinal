package com.example.proyectoosiris.ui.ocr


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel



class OcrViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Este es el fragmento ocr"
    }
    val text: LiveData<String> = _text
}