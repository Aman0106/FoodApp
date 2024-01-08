package com.example.food_app.fragments

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food_app.R
import com.example.food_app.activities.MealActivity
import com.example.food_app.adapters.FavouriteMealsAdapter
import com.example.food_app.dao.MealDatabase
import com.example.food_app.databinding.FragmentFavouritesBinding
import com.example.food_app.viewModel.FavouritesViewModel
import com.example.food_app.viewModel.FavouritesViewModelFactory

class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var favouritesViewModel: FavouritesViewModel
    private lateinit var favouriteMealsAdapter: FavouriteMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = MealDatabase.getInstance(requireContext())
        val favouritesViewModelFactory = FavouritesViewModelFactory(database)

        favouritesViewModel = ViewModelProvider(this, favouritesViewModelFactory)[FavouritesViewModel::class.java]

        favouriteMealsAdapter = FavouriteMealsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFavouritesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareFavouriteMealsAdapter()

        observeFavouritesData()
    }

    private fun prepareFavouriteMealsAdapter() {
        onMealItemClick()

        binding.recViewFavMeals.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = favouriteMealsAdapter
        }

        val spacing = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp) // Adjust spacing as needed
        binding.recViewFavMeals.addItemDecoration(GridSpacingItemDecoration(2, spacing, true, 0))
    }

    private fun onMealItemClick() {
        favouriteMealsAdapter.onItemClicked = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)

            startActivity(intent)
        }
    }

    private fun observeFavouritesData() {
        favouritesViewModel.observeFavouriteMealsLiveData().observe(viewLifecycleOwner) {
            favouriteMealsAdapter.setFavMeals(it)
        }
    }

}

class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val includeEdge: Boolean,
    private val headerNum: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: android.view.View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) - headerNum

        if (position >= 0) {
            val column = position % spanCount
            if (includeEdge) {
                outRect.left =
                    spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right =
                    (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) {
                    outRect.top = spacing
                }
                outRect.bottom = spacing
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right =
                    spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing
                }
            }
        } else {
            outRect.left = 0
            outRect.right = 0
            outRect.top = 0
            outRect.bottom = 0
        }
    }
}