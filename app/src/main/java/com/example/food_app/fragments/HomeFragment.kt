package com.example.food_app.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.food_app.R
import com.example.food_app.activities.MealActivity
import com.example.food_app.adapters.MealCategoriesAdapter
import com.example.food_app.adapters.MostPopularItemsAdapter
import com.example.food_app.databinding.FragmentHomeBinding
import com.example.food_app.pojo.Meal
import com.example.food_app.viewModel.HomeViewModel
import com.example.food_app.viewModel.SharedMainViewModel

class HomeFragment : Fragment() {

    val sharedMainViewModel: SharedMainViewModel by activityViewModels()

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var meal: Meal

    private lateinit var popularItemsAdapter: MostPopularItemsAdapter
    private lateinit var mealCategoriesAdapter: MealCategoriesAdapter

    companion object {
        const val MEAL_ID = "com.example.food_app.fragments.idMeal"
        const val MEAL_NAME = "com.example.food_app.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.food_app.fragments.thumbMeal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        popularItemsAdapter = MostPopularItemsAdapter()
        mealCategoriesAdapter = MealCategoriesAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareMostPopularItemsAdapter()
        prepareMealCategoriesAdapter()

        homeViewModel.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()

        homeViewModel.getMealByCategory("Seafood")
        observeMealsByCategory()                                //TODO: Add a progress bar

        onPopularItemClicker()

        homeViewModel.getMealsCategories()
        observeMealCategories()                                //TODO: Add a progress bar
        onCategoriesItemClicked()

    }


    private fun observeMealCategories() {
        homeViewModel.observeMealsCategories().observe(viewLifecycleOwner) { categories ->

            mealCategoriesAdapter.setMealCategories(categories)
        }
    }

    private fun onCategoriesItemClicked() {
        mealCategoriesAdapter.onItemClicked = {
            findNavController().navigate(R.id.action_homeFragment_to_mealsByCategory)

            sharedMainViewModel.setSHaredMeaCategory(it)
        }

    }

    private fun onPopularItemClicker() {
        popularItemsAdapter.onItemClicked = {
            val intent = Intent(activity, MealActivity::class.java)

            intent.putExtra(MEAL_ID, it.idMeal)
            intent.putExtra(MEAL_NAME, it.strMeal)
            intent.putExtra(MEAL_THUMB, it.strMealThumb)

            startActivity(intent)
        }
    }

    private fun prepareMostPopularItemsAdapter() {
        binding.recViewPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observeMealsByCategory() {
        homeViewModel.observeMealsCategory().observe(
            viewLifecycleOwner
        ) {
            popularItemsAdapter.setMeals(it)
        }
    }

    private fun prepareMealCategoriesAdapter() {
        binding.recViewTryByCat.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = mealCategoriesAdapter
        }
    }

    private fun onRandomMealClick() {
        binding.cardRandomMeal.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)

            startActivity(intent)
        }
    }

    private fun observeRandomMeal() {
        homeViewModel.observeRandomMealLiveData()
            .observe(viewLifecycleOwner
            ) { value ->
                Glide.with(this@HomeFragment)
                    .load(value.strMealThumb)
                    .into(binding.imgRandomMeal)

                meal = value

                binding.progressRandMeal.visibility = View.INVISIBLE
            }
    }


}