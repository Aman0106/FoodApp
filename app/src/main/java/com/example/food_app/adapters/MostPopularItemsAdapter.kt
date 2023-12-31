package com.example.food_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_app.databinding.PopularItemCardBinding
import com.example.food_app.pojo.MealByCategory

class MostPopularItemsAdapter: RecyclerView.Adapter<MostPopularItemsAdapter.PopularMealsViewHolder>() {

    private var mealsList = ArrayList<MealByCategory>()
    lateinit var onItemClicked: ((MealByCategory) ->  Unit)

    fun setMeals(mealsList: List<MealByCategory>) {
        this.mealsList = mealsList as ArrayList<MealByCategory>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealsViewHolder {
        return PopularMealsViewHolder(PopularItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }
    override fun onBindViewHolder(holder: PopularMealsViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgPopularMeal)

        holder.itemView.setOnClickListener {
            onItemClicked.invoke(mealsList[position])
        }
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }


    class PopularMealsViewHolder(val binding:PopularItemCardBinding): RecyclerView.ViewHolder(binding.root)
}