package com.example.project1.fragment

import android.content.Context
import android.os.Bundle
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
import com.example.project1.databinding.FragmentCategoryBinding
import com.example.project1.databinding.ItemViewProductBinding
import com.example.project1.roomdb.CartProduct
import com.example.project1.viewmodel.UserViewModels
import kotlinx.coroutines.launch
import kotlin.ClassCastException

class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private val viewModel : UserViewModels by viewModels()
    private  var category: String? = null
    private lateinit var adapterProduct: AdapterProduct
    private var cartListener : CartListener ? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        getProductCategory()
        onNavigationIconClicked()
        fetchCategoryProduct()
        onSearchMenuClicked()
        setToolBarTitle()
        return binding.root
    }

    private fun onNavigationIconClicked() {
        binding.tbSearchFragment.setNavigationOnClickListener{
            findNavController().navigate(R.id.action_categoryFragment_to_homeFragment)
        }
    }

    private fun onSearchMenuClicked() {
        binding.tbSearchFragment.setOnMenuItemClickListener{ menuItem ->
              when(menuItem.itemId){
                  R.id.searchMenu ->{
                      findNavController().navigate(R.id.action_categoryFragment_to_searchFragment)
                      true
                  }
                  else->{false}
              }
        }
    }

    private fun fetchCategoryProduct() {
        binding.shimmerViewContainer.visibility = View.VISIBLE
        lifecycleScope.launch {

            viewModel.getCategoryProduct(category!!).collect{
                if (it.isEmpty()){
                    binding.rvProduct.visibility = View.GONE
                    binding.tvText.visibility = View.VISIBLE
                }
                else{
                    binding.rvProduct.visibility = View.VISIBLE
                    binding.tvText.visibility = View.GONE
                }
                adapterProduct = AdapterProduct(::onAddButtonClicked,::onAddedButtonClicked)
                binding.rvProduct.adapter = adapterProduct
                adapterProduct.differ.submitList(it)
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

    private fun setToolBarTitle() {
        binding.tbSearchFragment.title = category
    }

    private fun getProductCategory() {
        val bundle = arguments
       category =  bundle?.getString("category")
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