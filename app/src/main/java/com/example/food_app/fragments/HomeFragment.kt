package com.example.food_app.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.food_app.R
import com.example.food_app.activities.MealActivity
import com.example.food_app.databinding.FragmentHomeBinding
import com.example.food_app.pojo.Meal
import com.example.food_app.pojo.MealList
import com.example.food_app.retrofit.RetrofitInstance
import com.example.food_app.viewModel.HomeViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.getRandomMeal()
        observeRandomMeal()

        onRandomMealClick()

    }

    private fun onRandomMealClick() {
        binding.cardRandomMeal.setOnClickListener{
            val intent = Intent(activity, MealActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeRandomMeal() {
        homeViewModel.observeRandomMealLiveData().observe(viewLifecycleOwner, object : Observer<Meal>{
            override fun onChanged(value: Meal) {
                Glide.with(this@HomeFragment)
                    .load(value.strMealThumb)
                    .into(binding.imgRandomMeal)
            }
        })
    }


}