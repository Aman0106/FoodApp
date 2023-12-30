package com.example.food_app.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.food_app.R
import com.example.food_app.activities.MealActivity
import com.example.food_app.adapters.MostPopularItemsAdapter
import com.example.food_app.databinding.FragmentHomeBinding
import com.example.food_app.pojo.Meal
import com.example.food_app.pojo.MealList
import com.example.food_app.pojo.MealOverview
import com.example.food_app.retrofit.RetrofitInstance
import com.example.food_app.viewModel.HomeViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var meal: Meal

    private lateinit var popularItemsAdapter: MostPopularItemsAdapter

    companion object {
        const val MEAL_ID = "com.example.food_app.fragments.idMeal"
        const val MEAL_NAME = "com.example.food_app.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.food_app.fragments.thumbMeal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        popularItemsAdapter = MostPopularItemsAdapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareMostPopularItemsAdapter()

        homeViewModel.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()

        homeViewModel.getMealByCategory("Seafood")
        observeMealsByCategory()

        onPopularItemClicker()

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
        homeViewModel.observeMealsCategory().observe(viewLifecycleOwner
        ) {
            popularItemsAdapter.setMeals(it as ArrayList<MealOverview>)
        }
    }

    private fun onRandomMealClick() {
        binding.cardRandomMeal.setOnClickListener{
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)

            startActivity(intent)
        }
    }

    private fun observeRandomMeal() {
        homeViewModel.observeRandomMealLiveData().observe(viewLifecycleOwner, object : Observer<Meal>{
            override fun onChanged(value: Meal) {
                Glide.with(this@HomeFragment)
                    .load(value.strMealThumb)
                    .into(binding.imgRandomMeal)

                meal = value
            }
        })
    }


}