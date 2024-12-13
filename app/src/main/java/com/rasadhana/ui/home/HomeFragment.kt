package com.rasadhana.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.search.SearchView.TransitionState
import com.rasadhana.R
import com.rasadhana.data.Result
import com.rasadhana.databinding.FragmentHomeBinding
import com.rasadhana.ui.login.LoginActivity
import com.rasadhana.ui.main.MainActivity
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var isSearchViewVisible = false

    private val homeViewModel: HomeViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.getSession().observe(viewLifecycleOwner) { user ->
            val name = user.name
            Log.d("HomeFragment", "User: $user")
            binding.tvUser.text = getString(R.string.greeting, name)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeSearchResultAdapter = RecipeSearchResultAdapter()

        homeViewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            recipeSearchResultAdapter.submitList(recipes)
        }

        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = recipeSearchResultAdapter
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)

            val handler = Handler(Looper.getMainLooper())

            searchView.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (count == 0) {
                        recipeSearchResultAdapter.submitList(emptyList())
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    val query = s.toString().trim()

                    handler.removeCallbacksAndMessages(null)
                    handler.postDelayed({
                        if (query.isNotEmpty()) {
                            homeViewModel.searchRecipes(query)
                        }
                    }, 500L)
                }
            })

            searchView.editText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                    searchView.hide()
                    (requireActivity() as MainActivity).setBottomNavVisibility(true)
                }
                false
            }

            searchView.addTransitionListener { _, _, newState ->
                if (newState === TransitionState.SHOWING) {
                    isSearchViewVisible = true
                    (requireActivity() as MainActivity).setBottomNavVisibility(false)
                } else if (newState === TransitionState.HIDING) {
                    (requireActivity() as MainActivity).setBottomNavVisibility(true)
                }
            }

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                if (isSearchViewVisible) {
                    searchView.hide()
                    isSearchViewVisible = false
                    (requireActivity() as MainActivity).setBottomNavVisibility(true)
                } else {
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        val recipeSuggestionAdapter = RecipeSuggestionAdapter { recipe ->
            if (recipe.isFavorite) {
                homeViewModel.deleteRecipeFromFavorite(recipe)
                showToast("${recipe.name} removed from favorites!")

            } else {
                homeViewModel.saveRecipeToFavorite(recipe)
                showToast("${recipe.name} added to favorites!")
            }
        }

        val recipeHistoryAdapter = RecipeHistoryAdapter { recipe ->
            if (recipe.isFavorite) {
                homeViewModel.deleteRecipeFromFavorite(recipe)
                showToast("${recipe.name} removed from favorites!")

            } else {
                homeViewModel.saveRecipeToFavorite(recipe)
                showToast("${recipe.name} added to favorites!")
            }
        }

        homeViewModel.getAllRecipe().observe(viewLifecycleOwner) { result ->
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

                        recipeSuggestionAdapter.submitList(listOfData)
                    }
                }
            }
        }

        homeViewModel.getHistoryRecommendationRecipes().observe(viewLifecycleOwner) { result ->
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

                        recipeHistoryAdapter.submitList(listOfData)
                    }
                }
            }
        }

        binding.rvRecipeMaybeYouLikeIt.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = recipeSuggestionAdapter
        }

        binding.rvRecipeHistory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = recipeHistoryAdapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}