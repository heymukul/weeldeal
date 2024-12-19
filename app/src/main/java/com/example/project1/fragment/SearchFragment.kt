package com.example.project1.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.project1.CartListener
import com.example.project1.Models.Product
import com.example.project1.R
import com.example.project1.adapter.AdapterProduct
import com.example.project1.databinding.FragmentSearchBinding
import com.example.project1.databinding.ItemViewProductBinding
import com.example.project1.roomdb.CartProduct
import com.example.project1.viewmodel.UserViewModels
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
private val viewModel : UserViewModels by viewModels()
private lateinit var binding: FragmentSearchBinding
private lateinit var adapterProduct: AdapterProduct
private var cartListener : CartListener? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        getAllTheProduct()
        searchProduct()
        backToHomeFragment()
        return binding.root
    }
    private fun searchProduct() {
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val query = s.toString().trim()
                adapterProduct.getFilter().filter(query)
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

    private fun backToHomeFragment() {
        binding.tvBack.setOnClickListener{

            findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
        }
    }

    private fun getAllTheProduct() {
        binding.shimmerViewContainer.visibility = View.VISIBLE
        lifecycleScope.launch{
            viewModel.fetchAllTheProduct().collect{
                if (it.isEmpty()){
                    binding.rvProduct.visibility = View.GONE
                    binding.tvText.visibility = View.VISIBLE
                }
                else{
                    binding.rvProduct.visibility = View.VISIBLE
                    binding.tvText.visibility = View.GONE
                }
                adapterProduct = AdapterProduct(::onAddButtonClicked, ::onAddedButtonClicked)
                binding.rvProduct.adapter = adapterProduct
                adapterProduct.differ.submitList(it)
                adapterProduct.originalList = it as ArrayList<Product>
                binding.shimmerViewContainer.visibility = View.GONE
            }

        }

    }
    private fun onAddButtonClicked(product: Product, productBinding: ItemViewProductBinding){
        productBinding.tvAdd.visibility = View.GONE
        productBinding.llProductCount.visibility = View.VISIBLE

        var itemCount = productBinding.tvProductCount.text.toString().toInt()
        itemCount++
        productBinding.tvProductCount.text = itemCount.toString()
        cartListener?.showCartLayout(1)
        product.itemCount = itemCount

        lifecycleScope.launch {
            cartListener?.savingCartItemCount(1)
            saveProductInRoom(product)
            viewModel.updateItemCount(product,itemCount)
        }
    }
    private fun onAddedButtonClicked(product: Product, productBinding: ItemViewProductBinding){
        var itemCountDec = productBinding.tvProductCount.text.toString().toInt()
        itemCountDec--
//        if (itemCountDec > 0){
//
//        }
        product.itemCount = itemCountDec

        lifecycleScope.launch {
            cartListener?.savingCartItemCount(-1)
            saveProductInRoom(product)
            viewModel.updateItemCount(product,itemCountDec)
        }
        lifecycleScope.launch {
            viewModel.deleteCartProduct(product.productRandomId!!)
        }
        Log.d("W",product.productRandomId!!)
        productBinding.tvAdd.visibility = View.VISIBLE
        productBinding.llProductCount.visibility = View.GONE
        productBinding.tvProductCount.text = "0"
        cartListener?.showCartLayout(-1)
        cartListener?.savingCartItemCount(-1)

    }
    private fun saveProductInRoom(product: Product) {
        val cartProduct = CartProduct(
            productId = product.productRandomId!!,
            productTitle = product.productTitle,
            productPurchase = product.purchaseYear,
            productCount = product.itemCount,
            productPrice = "₹" +  "${product.productPrice}",
            productImage = product.productImageUris?.get(0)!!,
            productCategory = product.productCategory,
            adminUid = product.adminUid,
        )
        lifecycleScope.launch {
            viewModel.insertCartProduct(cartProduct)
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is CartListener){
            cartListener = context
        }
        else{
            throw ClassCastException("Please implement cart listener")
        }

    }
}