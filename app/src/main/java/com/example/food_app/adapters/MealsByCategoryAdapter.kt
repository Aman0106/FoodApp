package com.example.food_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_app.databinding.MealsByCategoryItemBinding
import com.example.food_app.pojo.MealByCategory

class MealsByCategoryAdapter(): RecyclerView.Adapter<MealsByCategoryAdapter.MealsByCategoriesViewHolder>()  {

    private var mealsByCategory = ArrayList<MealByCategory>()
    lateinit var onItemClicked: ((MealByCategory) ->  Unit)

    fun setMeals(mealsByCategory: List<MealByCategory>) {
        this.mealsByCategory = mealsByCategory as ArrayList<MealByCategory>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsByCategoriesViewHolder {
        return MealsByCategoriesViewHolder(
            MealsByCategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MealsByCategoriesViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsByCategory[position].strMealThumb)
            .into(holder.binding.imgMealThumb)
        holder.binding.tvMealName.text = mealsByCategory[position].strMeal

        holder.binding.cardMealsByCat.setOnClickListener {
            onItemClicked.invoke(mealsByCategory[position])
        }
    }

    override fun getItemCount() = mealsByCategory.size

    inner class MealsByCategoriesViewHolder(val binding: MealsByCategoryItemBinding): RecyclerView.ViewHolder(binding.root)
}