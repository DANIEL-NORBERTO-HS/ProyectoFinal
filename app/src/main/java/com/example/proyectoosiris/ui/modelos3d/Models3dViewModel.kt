package com.example.proyectoosiris.ui.modelos3d

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.ar.core.Anchor
import com.google.ar.sceneform.rendering.ModelRenderable
import org.w3c.dom.Node

class Models3dViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Este es el fragmento de los modelos 3d"
    }
    val text: LiveData<String> = _text


}