package com.example.food_app.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.food_app.pojo.MealsByCategoryList
import com.example.food_app.pojo.Meal
import com.example.food_app.pojo.MealList
import com.example.food_app.pojo.MealByCategory
import com.example.food_app.pojo.MealCategory
import com.example.food_app.pojo.MealsCategoryList
import com.example.food_app.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(): ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var mealsByCategoryLiveData = MutableLiveData<List<MealByCategory>>()

    private var mealsCategoriesLiveData = MutableLiveData<List<MealCategory>>()

    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object: Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals!![0]
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
        RetrofitInstance.api.getMealsByCategory(category).enqueue(object : Callback<MealsByCategoryList> {
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                response.body()?.let {
                    mealsByCategoryLiveData.postValue(it.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.e("MealAPI", "getMealByCategory unable to fetch")
            }
        })
    }

    fun observeMealsCategory(): LiveData<List<MealByCategory>> = mealsByCategoryLiveData

    fun getMealsCategories() {
        RetrofitInstance.api.getMealsCategories().enqueue(object : Callback<MealsCategoryList> {
            override fun onResponse(
                call: Call<MealsCategoryList>,
                response: Response<MealsCategoryList>
            ) {
                if(response.body() != null) {
                    mealsCategoriesLiveData.value = response.body()!!.categories
                }
            }

            override fun onFailure(call: Call<MealsCategoryList>, t: Throwable) {
                Log.e("MealAPI", "getMealsCategories unable to fetch")
            }
        })
    }

    fun observeMealsCategories(): LiveData<List<MealCategory>> = mealsCategoriesLiveData


}
