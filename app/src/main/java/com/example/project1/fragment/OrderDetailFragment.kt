package com.example.project1.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.project1.R
import com.example.project1.adapter.AdapterCartProducts
import com.example.project1.databinding.FragmentOrderDetailBinding
import com.example.project1.viewmodel.UserViewModels
import kotlinx.coroutines.launch


class OrderDetailFragment : Fragment() {

    private lateinit var binding: FragmentOrderDetailBinding
    private val viewModel: UserViewModels by viewModels()
    private lateinit var adapterCartProducts: AdapterCartProducts
    private var status = 0
    private var orderId = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailBinding.inflate(layoutInflater)
        getValue()
        settingStatus()
        onBackButtonClicked()
        lifecycleScope.launch { getOrderProduct() }
        return binding.root
    }

    suspend private fun getOrderProduct() {
         viewModel.getOrderProduct(orderId).collect{cartList ->
             adapterCartProducts = AdapterCartProducts()
             binding.rvProductItems.adapter = adapterCartProducts
             adapterCartProducts.differ.submitList(cartList)
         }
    }
    private fun onBackButtonClicked() {
        binding.tbOrderDetailFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_orderDetailFragment_to_orderFragment)
        }

    }
    private fun settingStatus() {
        when(status){
            0->{
                binding.iv1.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.blue)
            }
            1->{
                binding.iv1.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.blue)
                binding.view1.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.blue)
                binding.iv2.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.blue)
            }
            2->{
                binding.iv1.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.blue)
                binding.view1.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.blue)
                binding.iv2.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.blue)
                binding.view2.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.blue)
                binding.iv3.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.blue)
            }
            3->{
                binding.iv1.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.blue)
                binding.view1.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.blue)
                binding.iv2.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.blue)
                binding.view2.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.blue)
                binding.iv3.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.blue)
                binding.view3.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.blue)
                binding.iv4.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.blue)
            }
        }
    }

    private fun getValue() {
        val bundle = arguments
        status = bundle?.getInt("Status")!!
        orderId = bundle.getString("OrderId").toString()
    }

}