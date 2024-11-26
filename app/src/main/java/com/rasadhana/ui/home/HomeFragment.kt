package com.rasadhana.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (requireActivity() as MainActivity).supportActionBar?.hide()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homeViewModel: HomeViewModel by inject()

        homeViewModel.getSession().observe(viewLifecycleOwner) { user ->
            val token = user.token

            homeViewModel.getUserData(token).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Error -> {
                            showLoading(false)
                            showToast(result.error)
                        }
                        Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)

                            if (result.data.success) {
                                val response = result.data
                                val userData = response.data

                                binding.tvUser.text = getString(R.string.greeting, userData.name)
                            }
                        }
                    }
                }
            }
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