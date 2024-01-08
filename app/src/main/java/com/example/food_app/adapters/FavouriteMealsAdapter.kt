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
import com.example.food_app.databinding.FragmentFavouritesBinding
import com.example.food_app.databinding.MealsByCategoryItemBinding
import com.example.food_app.pojo.Meal

class FavouriteMealsAdapter(): RecyclerView.Adapter<FavouriteMealsAdapter.FavouriteMealsViewHolder>() {

    private var favMeals = ArrayList<Meal>()
    lateinit var onItemClicked: ((Meal) ->  Unit)

    fun setFavMeals(favMealsList: List<Meal>) {
        favMeals = favMealsList as ArrayList<Meal>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteMealsViewHolder {
        return FavouriteMealsViewHolder(
            MealsByCategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavouriteMealsViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(favMeals[position].strMealThumb).listener(object : RequestListener<Drawable> {
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

        holder.binding.tvMealName.text = favMeals[position].strMeal

        holder.binding.cardMealsByCat.setOnClickListener {
            onItemClicked.invoke(favMeals[position])
        }
    }

    override fun getItemCount() = favMeals.size

    class FavouriteMealsViewHolder(val binding: MealsByCategoryItemBinding): RecyclerView.ViewHolder(binding.root)
}