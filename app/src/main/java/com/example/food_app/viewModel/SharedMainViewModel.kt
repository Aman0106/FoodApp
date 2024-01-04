package com.example.food_app.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.food_app.pojo.MealCategory

class SharedMainViewModel: ViewModel() {
    private val sharedMealCategory = MutableLiveData<MealCategory>()

    fun setSHaredMeaCategory(mealCategory: MealCategory) {
        sharedMealCategory.value = mealCategory
    }

    fun observerSharedMealCategory() : LiveData<MealCategory> = sharedMealCategory
}