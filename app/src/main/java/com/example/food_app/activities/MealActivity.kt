package com.example.food_app.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.food_app.R
import com.example.food_app.databinding.ActivityMealBinding
import com.example.food_app.fragments.HomeFragment
import com.example.food_app.pojo.Meal
import com.example.food_app.viewModel.MealViewModel

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var mealViewModel: MealViewModel

    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLink: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingCase()

        getMealInformationFromIntent()
        setInformationInViews()

        mealViewModel = ViewModelProvider(this)[MealViewModel::class.java]
        mealViewModel.getMealById(mealId)
        observeMealLiveData()

        onYoutubeImageClick()
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))

            startActivity(intent)
        }
    }

    private fun observeMealLiveData() {
        mealViewModel.observerMealLiveData().observe(this, object : Observer<Meal> {
            override fun onChanged(value: Meal) {

                binding.tvCategory.text = "Category: ${value.strCategory}"
                binding.tvOrigin.text = "Origin: ${value.strArea}"
                binding.tvMealInstructions.text = value.strInstructions
                youtubeLink = value.strYoutube

                onResponseCase()
            }
        })
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)
        binding.collapsingToolbarMeal.title = mealName
    }

    private fun getMealInformationFromIntent() {
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun loadingCase() {
        binding.progressHorizontal.visibility = View.VISIBLE
        binding.floatingButtonMeal.visibility = View.INVISIBLE
        binding.nestedScrollView.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
    }

    private fun onResponseCase() {
        binding.progressHorizontal.visibility = View.INVISIBLE
        binding.floatingButtonMeal.visibility = View.VISIBLE
        binding.nestedScrollView.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }
}