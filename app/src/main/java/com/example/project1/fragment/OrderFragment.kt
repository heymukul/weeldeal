package com.example.project1.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.project1.Models.OrderedItem
import com.example.project1.R
import com.example.project1.adapter.AdapterOrder
import com.example.project1.databinding.FragmentOrderBinding
import com.example.project1.viewmodel.UserViewModels
import kotlinx.coroutines.launch


class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding
    private val viewModel: UserViewModels by viewModels()
    private lateinit var adapterOrder: AdapterOrder
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(layoutInflater)
        onBackButtonClicked()
        getAllOrders()
        return binding.root
    }

    private fun getAllOrders() {
        binding.shimmerViewContainer.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.getAllOrder().collect { orderList ->
                if (orderList.isNotEmpty()) {
                    val orderedList = ArrayList<OrderedItem>()
                    for (order in orderList) {
                        val title = StringBuilder()
                        var totalPrice = 0
                        for (product in order.orderList!!) {
                            val price = product.productPrice?.substring(1)?.toInt()
                            val itemCount = product.productCount!!
                            totalPrice += (price?.times(itemCount)!!)
                            title.append("${product.productCategory} ,")
                        }
                        val orderedItem = OrderedItem(
                            order.orderId,
                            order.orderDate,
                            order.orderStatus,
                            title.toString(),
                            totalPrice
                        )
                        orderedList.add(orderedItem)
                    }
                    adapterOrder = AdapterOrder(requireContext(),::onOrderItemViewClicked)
                    binding.rvOrder.adapter = adapterOrder
                    adapterOrder.differ.submitList(orderedList)
                    binding.shimmerViewContainer.visibility = View.GONE
                }
            }
        }
    }
    fun onOrderItemViewClicked(orderedItem: OrderedItem){
           val bundle = Bundle()
           bundle.putInt("Status",orderedItem.itemStatus!!)
           bundle.putString("orderId",orderedItem.orderId)
        findNavController().navigate(R.id.action_orderFragment_to_orderDetailFragment,bundle)
    }

    private fun onBackButtonClicked() {
        binding.tbOrderFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_orderFragment_to_profileFreagment)
        }

    }
}