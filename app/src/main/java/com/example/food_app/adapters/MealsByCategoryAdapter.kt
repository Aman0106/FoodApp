package com.example.food_app.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
            .load(mealsByCategory[position].strMealThumb).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.binding.progressMeal.visibility = View.INVISIBLE

                    return false
                }
            })
            .into(holder.binding.imgMealThumb)

        holder.binding.tvMealName.text = mealsByCategory[position].strMeal

        holder.binding.cardMealsByCat.setOnClickListener {
            onItemClicked.invoke(mealsByCategory[position])
        }
    }

    override fun getItemCount() = mealsByCategory.size

    inner class MealsByCategoriesViewHolder(val binding: MealsByCategoryItemBinding): RecyclerView.ViewHolder(binding.root)
}