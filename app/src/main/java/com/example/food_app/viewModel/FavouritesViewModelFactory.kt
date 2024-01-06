package com.example.food_app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.food_app.dao.MealDatabase

class FavouritesViewModelFactory(private val mealsDatabase: MealDatabase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavouritesViewModel(mealsDatabase) as T
    }
}