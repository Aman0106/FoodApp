package com.example.food_app.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.food_app.activities.MealActivity
import com.example.food_app.adapters.MealsAdapter
import com.example.food_app.databinding.FragmentSearchBinding
import com.example.food_app.pojo.Meal
import com.example.food_app.viewModel.SearchViewModel


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var mealsAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        mealsAdapter = MealsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareFavouriteMealsAdapter()
        handleSearchView()
        observeMealsBySearch()

    }

    private fun prepareFavouriteMealsAdapter() {
        onMealItemClick()

        binding.recMeals.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mealsAdapter
        }

        val spacing = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp) // Adjust spacing as needed
        binding.recMeals.addItemDecoration(GridSpacingItemDecoration(2, spacing, true, 0))
    }

    private fun onMealItemClick() {
        mealsAdapter.onItemClicked = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)

            startActivity(intent)
        }
    }

    private fun observeMealsBySearch() {
        searchViewModel.observeMealsLiveData().observe(viewLifecycleOwner) {
            if(it != null)
                mealsAdapter.setFavMeals(it)
//            Toast.makeText(context, "${it.size} meals found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleSearchView() {
        binding.edtSearch.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                val subStr = binding.edtSearch.text.toString()
                searchViewModel.getMealsBySubString(subStr)
                true
            } else {
                false
            }
        }
    }
}