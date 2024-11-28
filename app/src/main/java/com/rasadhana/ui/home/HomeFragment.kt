package com.rasadhana.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rasadhana.R
import com.rasadhana.data.Result
import com.rasadhana.databinding.FragmentHomeBinding
import com.rasadhana.ui.main.MainActivity
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (requireActivity() as MainActivity).supportActionBar?.hide()

        homeViewModel.getSession().observe(viewLifecycleOwner) { user ->
            val name = user.name
            Log.d("HomeFragment", "User: $user")
            binding.tvUser.text = getString(R.string.greeting, name)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeSuggestionAdapter = RecipeSuggestionAdapter()

        homeViewModel.getDummyRecipes().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }
                    Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)

                        val listOfData = result.data

                        Log.d("HomeFragment", "List of Data: $listOfData")

                        recipeSuggestionAdapter.submitList(listOfData)
                    }
                }
            }
        }

        binding.rvRecipeMaybeYouLikeIt.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = recipeSuggestionAdapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (requireActivity() as MainActivity).supportActionBar?.show()
    }
}