package com.example.proyectoosiris.ui.ocr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.proyectoosiris.databinding.FragmentOcrBinding


class OcrFragment : Fragment() {
    private var _binding: FragmentOcrBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val ocrViewModel =
            ViewModelProvider(this).get(OcrViewModel::class.java)

        _binding = FragmentOcrBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textOcr
        ocrViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}