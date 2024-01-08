package com.example.food_app.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app.dao.MealDatabase
import com.example.food_app.pojo.Meal
import com.example.food_app.pojo.MealList
import com.example.food_app.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(
    private val mealDatabase: MealDatabase
) : ViewModel() {
    private val mealLiveData = MutableLiveData<Meal>()

    fun getMealById(id: String) {
        RetrofitInstance.api.getMealById(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val meal: Meal = response.body()!!.meals!![0]
                    mealLiveData.value = meal

                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {

                Log.d("Error", "Unable to connect")
            }
        })
    }

    fun observerMealLiveData(): LiveData<Meal> = mealLiveData

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().insertMeal(meal)
        }
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().deleteMeal(meal)
        }
    }
}