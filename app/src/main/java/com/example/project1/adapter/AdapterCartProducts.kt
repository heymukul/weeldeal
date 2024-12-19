package com.example.project1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.project1.databinding.ItemViewCartProductBinding
import com.example.project1.roomdb.CartProduct
import com.google.common.graph.ElementOrder.Type

class AdapterCartProducts: RecyclerView.Adapter<AdapterCartProducts.CartProductViewHolder>() {
    class CartProductViewHolder (val binding: ItemViewCartProductBinding): ViewHolder(binding.root)

    val diffUtil = object :  DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
         return  CartProductViewHolder(ItemViewCartProductBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        val product = differ.currentList[position]

        holder.binding.apply {
            Glide.with(holder.itemView).load(product.productImage).into(ivProductImage)
            tvProductTitle.text = product.productTitle
            tvPurchaseYear.text = product.productPurchase.toString()
            tvProductPrice.text = product.productPrice
            tvProductCount.text = product.productCount.toString()
        }
    }
}