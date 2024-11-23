package com.rasadhana.ui.kulkas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rasadhana.databinding.FragmentKulkasBinding

class KulkasFragment : Fragment() {

    private var _binding: FragmentKulkasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val kulkasViewModel =
            ViewModelProvider(this)[KulkasViewModel::class.java]

        _binding = FragmentKulkasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textKulkas
        kulkasViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}