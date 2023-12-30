package com.example.food_app.viewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.food_app.pojo.CategoryList
import com.example.food_app.pojo.Meal
import com.example.food_app.pojo.MealList
import com.example.food_app.pojo.MealOverview
import com.example.food_app.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(): ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var mealsByCategoryLiveData = MutableLiveData<List<MealOverview>>()

    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object: Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal

                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {

                Log.d("Error", "Unable to connect")
            }
        })
    }

    fun observeRandomMealLiveData(): LiveData<Meal> = randomMealLiveData

    fun getMealByCategory(category: String) {
        RetrofitInstance.api.getMealsByCategory(category).enqueue(object : Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if(response.body() != null) {
                    mealsByCategoryLiveData.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {

            }
        })
    }

    fun observeMealsCategory(): LiveData<List<MealOverview>> = mealsByCategoryLiveData

}