package com.example.food_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_app.databinding.CategoryItemBinding
import com.example.food_app.pojo.MealCategory

class MealCategoriesAdapter(): RecyclerView.Adapter<MealCategoriesAdapter.MealCategoriesViewHolder>() {

    private var mealsCategories = ArrayList<MealCategory>()
    lateinit var onItemClicked: ((MealCategory) -> Unit)

    fun setMealCategories(list: List<MealCategory>) {
        mealsCategories = list as ArrayList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealCategoriesViewHolder {
        return MealCategoriesViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MealCategoriesViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsCategories[position].strCategoryThumb)
            .into(holder.binding.imgCategory)
        holder.binding.tvCategoryName.text = mealsCategories[position].strCategory

        holder.binding.parentCategoryItem.setOnClickListener {
            onItemClicked.invoke(mealsCategories[position])
        }
    }

    override fun getItemCount(): Int = mealsCategories.size

    inner class MealCategoriesViewHolder(val binding : CategoryItemBinding): RecyclerView.ViewHolder(binding.root)
}
