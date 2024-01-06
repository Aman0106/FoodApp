package com.example.food_app.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.food_app.dao.MealDatabase
import com.example.food_app.databinding.ActivityMealBinding
import com.example.food_app.fragments.HomeFragment
import com.example.food_app.pojo.Meal
import com.example.food_app.viewModel.MealViewModel
import com.example.food_app.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var mealViewModel: MealViewModel

    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private var currentMeal: Meal? = null
    private lateinit var youtubeLink: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingCase()

        getMealInformationFromIntent()
        setInformationInViews()

        val mealDatabase = MealDatabase.getInstance(this)
        val mealViewModelFactory = MealViewModelFactory(mealDatabase)

        mealViewModel = ViewModelProvider(this, mealViewModelFactory)[MealViewModel::class.java]
        mealViewModel.getMealById(mealId)
        observeMealLiveData()

        onFavouriteButtonClick()

        onYoutubeImageClick()
    }

    private fun onFavouriteButtonClick() {
        binding.floatingButtonMeal.setOnClickListener {
            currentMeal?.let {
                mealViewModel.insertMeal(currentMeal!!)
                Toast.makeText(this, "Recipe Added to Favourites", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))

            startActivity(intent)
        }
    }

    private fun observeMealLiveData() {
        mealViewModel.observerMealLiveData().observe(this
        ) { value ->
            binding.tvCategory.text = "Category: ${value.strCategory}"
            binding.tvOrigin.text = "Origin: ${value.strArea}"
            binding.tvMealInstructions.text = value.strInstructions
            youtubeLink = value.strYoutube.toString()

            currentMeal = value;

            onResponseCase()
        }
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