package com.example.proyectoosiris.ui.modelos3d

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.proyectoosiris.R
import com.example.proyectoosiris.databinding.FragmentModels3dBinding
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.android.synthetic.main.nav_header_main.textView

class Model3dFragment : Fragment() {
    // object of ArFragment Class
    private var arCam: ArFragment? = null
    private lateinit var _binding: FragmentModels3dBinding

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        _binding= FragmentModels3dBinding.inflate(layoutInflater)
        val view = _binding.root
    }}
    /*private void camara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }
}*/