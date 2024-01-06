package com.example.food_app.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.food_app.activities.MealActivity
import com.example.food_app.adapters.FavouriteMealsAdapter
import com.example.food_app.dao.MealDatabase
import com.example.food_app.databinding.FragmentFavouritesBinding
import com.example.food_app.viewModel.FavouritesViewModel
import com.example.food_app.viewModel.FavouritesViewModelFactory

class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var favouritesViewModel: FavouritesViewModel
    private lateinit var favouriteMealsAdapter: FavouriteMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = MealDatabase.getInstance(requireContext())
        val favouritesViewModelFactory = FavouritesViewModelFactory(database)

        favouritesViewModel = ViewModelProvider(this, favouritesViewModelFactory)[FavouritesViewModel::class.java]

        favouriteMealsAdapter = FavouriteMealsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFavouritesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareFavouriteMealsAdapter()

        observeFavouritesData()
    }

    private fun prepareFavouriteMealsAdapter() {
        onMealItemClick()

        binding.recViewFavMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favouriteMealsAdapter
        }
    }

    private fun onMealItemClick() {
        favouriteMealsAdapter.onItemClicked = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)

            startActivity(intent)
        }
    }

    private fun observeFavouritesData() {
        favouritesViewModel.observeFavouriteMealsLiveData().observe(viewLifecycleOwner) {
            favouriteMealsAdapter.setFavMeals(it)
        }
    }

}