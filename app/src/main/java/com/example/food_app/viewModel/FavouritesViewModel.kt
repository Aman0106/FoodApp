package com.example.food_app.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app.dao.MealDatabase
import com.example.food_app.pojo.Meal
import kotlinx.coroutines.launch

class FavouritesViewModel(private val mealDatabase: MealDatabase): ViewModel() {

    private var favouriteMealsLiveData = mealDatabase.mealDao().getAllFavMeals()

    fun observeFavouriteMealsLiveData(): LiveData<List<Meal>> = favouriteMealsLiveData
}