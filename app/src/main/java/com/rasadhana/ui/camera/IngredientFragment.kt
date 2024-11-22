package com.rasadhana.ui.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rasadhana.databinding.FragmentIngredientBinding

class IngredientFragment : Fragment() {

    private var _binding: FragmentIngredientBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val ingredientViewModel =
            ViewModelProvider(this)[IngredientViewModel::class.java]

        _binding = FragmentIngredientBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textIngredient
        ingredientViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}