package com.example.food_app.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.food_app.pojo.Meal
import com.example.food_app.pojo.MealList
import com.example.food_app.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel() : ViewModel() {
    private var mealsLiveData = MutableLiveData<List<Meal>?>()

    fun getMealsBySubString(subString: String) {
        RetrofitInstance.api.getMealsBySubString(subString).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() != null && response.body()!!.meals != null) {
                    Log.d("api", subString)
                    mealsLiveData.value = response.body()!!.meals!!
                }
                else
                    mealsLiveData.value = listOf()

            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("SearchMealAPI", "Search meal unable to fetch")
            }
        })
    }

    fun observeMealsLiveData(): LiveData<List<Meal>?> = mealsLiveData
}