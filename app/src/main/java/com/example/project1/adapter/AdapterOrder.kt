package com.example.project1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.project1.Models.OrderedItem
import com.example.project1.R
import com.example.project1.databinding.ItemViewOrdersBinding

class AdapterOrder(val context: Context, val   onOrderItemViewClicked: (OrderedItem) -> Unit) : RecyclerView.Adapter<AdapterOrder.OrderViewHolder> () {
    class OrderViewHolder(val binding: ItemViewOrdersBinding) : ViewHolder(binding.root)

    val diffUtil = object : DiffUtil.ItemCallback<OrderedItem>() {
        override fun areItemsTheSame(oldItem: OrderedItem, newItem: OrderedItem): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: OrderedItem, newItem: OrderedItem): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            ItemViewOrdersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = differ.currentList[position]
        holder.binding.apply {
             tvOrderTitle.text = order.itemTitle
            tvOrderDate.text  = order.itemDate
            tvOrderPrice.text = "₹${order.itemPrice.toString()}"
            when(order.itemStatus) {
                0->{
                    tvOrderStatus.text = "Ordered"
                    tvOrderStatus.backgroundTintList = ContextCompat.getColorStateList(holder.itemView.context, R.color.yellow)
                }
                1->{
                    tvOrderStatus.text = "Received"
                    tvOrderStatus.backgroundTintList = ContextCompat.getColorStateList(holder.itemView.context, R.color.blue)
                }
                2-> {
                    tvOrderStatus.text = "Dispatched"
                    tvOrderStatus.backgroundTintList = ContextCompat.getColorStateList(holder.itemView.context, R.color.green)
                }
                3->{
                    tvOrderStatus.text = "Delivered"
                    tvOrderStatus.backgroundTintList = ContextCompat.getColorStateList(holder.itemView.context, R.color.orange)
                }
            }
        }
        holder.itemView.setOnClickListener{
            onOrderItemViewClicked(order)
        }
    }
}