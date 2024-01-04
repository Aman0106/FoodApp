package com.example.food_app.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.food_app.pojo.MealByCategory
import com.example.food_app.pojo.MealCategory
import com.example.food_app.pojo.MealsByCategoryList
import com.example.food_app.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealByCategoryViewModel: ViewModel() {
    private var mealsByCategoryLiveData = MutableLiveData<List<MealByCategory>>()

    fun getMealsByCategory(category: String) {
        RetrofitInstance.api.getMealsByCategory(category).enqueue(object :
            Callback<MealsByCategoryList> {
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

    fun observerMealsByCategoryLiveData(): LiveData<List<MealByCategory>> = mealsByCategoryLiveData
}