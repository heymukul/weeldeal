package com.example.project1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.project1.Models.Category
import com.example.project1.databinding.ItemVewProductCatogryBinding
import java.text.FieldPosition

class AdapterCategory(
    val categoryList: ArrayList<Category>,
    val onCategoryIconClicked: (Category) -> Unit
) : RecyclerView.Adapter<AdapterCategory.CategoryViewHolder>(){
    class CategoryViewHolder (val binding: ItemVewProductCatogryBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(ItemVewProductCatogryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            val  category = categoryList[position]
            holder.binding.apply {
                ivCategory.setImageResource(category.image)
                tvCategoryTitle.text = category.title
            }
        holder.itemView.setOnClickListener{
            onCategoryIconClicked(category)
        }
    }




}