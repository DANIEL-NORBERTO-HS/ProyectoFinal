package com.example.proyectoosiris.ui.modelos3d

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.example.proyectoosiris.databinding.FragmentModels3dBinding

class Model3dFragment : Fragment() {

    private var _binding: FragmentModels3dBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(Models3dViewModel::class.java)

        _binding = FragmentModels3dBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textModel3d
        slideshowViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}