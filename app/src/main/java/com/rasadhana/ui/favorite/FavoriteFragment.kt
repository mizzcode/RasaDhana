package com.rasadhana.ui.favorite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.rasadhana.databinding.FragmentFavoriteBinding
import org.koin.android.ext.android.inject

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null

    private val binding get() = _binding!!

    private val favoriteViewModel: FavoriteViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoriteAdapter = FavoriteAdapter { recipe ->
            favoriteViewModel.deleteRecipeFromFavorite(recipe)
            Toast.makeText(requireContext(), "${recipe.name} removed from favorites!", Toast.LENGTH_SHORT).show()
        }

        favoriteViewModel.getRecipesFavorite().observe(viewLifecycleOwner) { favoriteRecipes ->
            Log.d("favoriteFragment", "$favoriteRecipes")
            if (favoriteRecipes != null) {
                favoriteAdapter.submitList(favoriteRecipes)
                Log.d("FavoriteFragment", "List submitted: ${favoriteRecipes.size}")
            }

            binding.rvFavoriteRecipes.apply {
                layoutManager = GridLayoutManager(context, 2)
                setHasFixedSize(true)
                adapter = favoriteAdapter
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}