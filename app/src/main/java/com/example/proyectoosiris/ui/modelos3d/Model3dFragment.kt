package com.example.proyectoosiris.ui.modelos3d

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.proyectoosiris.databinding.ActivityAuthBinding

import com.example.proyectoosiris.databinding.FragmentModels3dBinding
import kotlinx.android.synthetic.main.nav_header_main.textView

class Model3dFragment : Fragment() {

    private lateinit var _binding: FragmentModels3dBinding

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        _binding= FragmentModels3dBinding.inflate(layoutInflater)
        val view = _binding.root
    }

}