package com.example.food_app.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.food_app.activities.MealActivity
import com.example.food_app.adapters.MealsByCategoryAdapter
import com.example.food_app.databinding.FragmentMealsByCategoryBinding
import com.example.food_app.pojo.MealCategory
import com.example.food_app.viewModel.MealByCategoryViewModel
import com.example.food_app.viewModel.SharedMainViewModel


class MealsByCategoryFragment : Fragment() {

    private val sharedMainViewModel: SharedMainViewModel by activityViewModels()

    private lateinit var binding: FragmentMealsByCategoryBinding

    private lateinit var mealByCategoryViewModel: MealByCategoryViewModel
    private lateinit var currentMealCategory: MealCategory

    private lateinit var mealsByCategoryAdapter: MealsByCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressAction()
        mealByCategoryViewModel = ViewModelProvider(this)[MealByCategoryViewModel::class.java]

        mealsByCategoryAdapter = MealsByCategoryAdapter()

    }

    private fun onBackPressAction() {
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealsByCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareMealsByCategoryAdapter()

        currentMealCategory = sharedMainViewModel.observerSharedMealCategory().value!!

        mealByCategoryViewModel.getMealsByCategory(currentMealCategory.strCategory)
        observeMealCategories()

        setBackButtonCLick()
    }

    private fun setBackButtonCLick() {
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun prepareMealsByCategoryAdapter() {
        onMealItemClick()

        binding.recViewMealsByCategory.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = mealsByCategoryAdapter
        }
    }

    private fun onMealItemClick() {
        mealsByCategoryAdapter.onItemClicked = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)

            startActivity(intent)
        }
    }

    private fun observeMealCategories() {
        mealByCategoryViewModel.observerMealsByCategoryLiveData().observe(viewLifecycleOwner) {
            binding.tvMealsCount.text = "${currentMealCategory.strCategory}: ${it.size}"
            mealsByCategoryAdapter.setMeals(it)

            binding.progressMeals.visibility = View.GONE
            binding.tvMealsCount.visibility = View.VISIBLE
        }
    }

}