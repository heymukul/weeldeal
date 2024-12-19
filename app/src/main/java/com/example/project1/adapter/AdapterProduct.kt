package com.example.project1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.denzcoskun.imageslider.models.SlideModel
import com.example.project1.FilteringProduct
import com.example.project1.Models.Product
import com.example.project1.databinding.ItemViewProductBinding

class AdapterProduct(
    val onAddButtonClicked: (Product, ItemViewProductBinding) -> Unit,
    val onAddedButtonClicked: (Product, ItemViewProductBinding) -> Unit
) : RecyclerView.Adapter<AdapterProduct.ProductViewHolder>(),Filterable{
    class ProductViewHolder(val binding: ItemViewProductBinding): ViewHolder(binding.root) {

    }

    val diffUtil = object : DiffUtil.ItemCallback<Product>(){
         override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
             return oldItem.productRandomId == newItem.productRandomId
         }

         override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
             return oldItem == newItem
         }

     }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
         return ProductViewHolder(ItemViewProductBinding.inflate(LayoutInflater.from(parent.context),parent , false))
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
       val product = differ.currentList[position]
        holder.binding.apply {
            val imageList = ArrayList<SlideModel>()
            val productImage = product.productImageUris

            for (i in 0 until productImage?.size!!){
                imageList.add(SlideModel(product.productImageUris!![i].toString()))
            }
            ivImageSlider.setImageList(imageList)
            tvProductTitle.text = product.productTitle
            tvPurchaseYear.text = product.purchaseYear.toString()
            tvProductPrice.text = "₹"+ product.productPrice

            if (product.itemCount!! > 0){
                tvProductCount.text = product.itemCount.toString()
                tvAdd.visibility = View.GONE
                llProductCount.visibility = View.VISIBLE
            }

            tvAdd.setOnClickListener {
               onAddButtonClicked(product,this)
            }
            tvAdded.setOnClickListener {
                onAddedButtonClicked(product,this)
            }
        }
    }


    val filter: FilteringProduct? = null
    var originalList  = ArrayList<Product>()
    override fun getFilter(): Filter {
        if (filter == null) return FilteringProduct(this,originalList)
        return filter
    }

}