package com.example.project1.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import com.example.project1.CartListener
import com.example.project1.adapter.AdapterCartProducts
import com.example.project1.databinding.ActivityUserMainBinding
import com.example.project1.databinding.BsCartProductBinding
import com.example.project1.roomdb.CartProduct
import com.example.project1.viewmodel.UserViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog

class UserMainActivity : AppCompatActivity(), CartListener  {
    lateinit var binding: ActivityUserMainBinding
    private val viewModel : UserViewModels by viewModels()
    private lateinit var cartProductList : List<CartProduct>
    private lateinit var adapterCartProducts: AdapterCartProducts
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getAllCartProducts()
        getTotalItemCountInCart()
        onCartClicked()
        omNextBottomClicked()
    }



    private fun omNextBottomClicked() {
        binding.btnNext.setOnClickListener{
            startActivity(Intent(this,OrderPlaceActivity::class.java))

        }
    }

    private fun getAllCartProducts() {
            viewModel.getAll().observe(this){

                   cartProductList = it
            }

    }

    private fun onCartClicked() {
      binding.llItemCount.setOnClickListener {
          val bsCartProductBinding = BsCartProductBinding.inflate(LayoutInflater.from(this))

          val bs = BottomSheetDialog(this)
          bs.setContentView(bsCartProductBinding.root)
          bsCartProductBinding.tvNumberOfProductCount.text = binding.tvNumberOfProductCount.text
          bsCartProductBinding.btnNext.setOnClickListener{
              startActivity(Intent(this,OrderPlaceActivity::class.java))
          }
          adapterCartProducts = AdapterCartProducts()
          bsCartProductBinding.rvProductCart.adapter = adapterCartProducts
          adapterCartProducts.differ.submitList(cartProductList)
          bs.show()
      }
    }

    private fun getTotalItemCountInCart() {
        viewModel.fetchTotalCartItemCount().observe(this){
            if(it>0){
                binding.llCart.visibility = View.VISIBLE
                binding.tvNumberOfProductCount.text = it.toString()
            }
            else{
                binding.llCart.visibility = View.GONE
            }
        }
    }

    override fun showCartLayout(itemCount: Int) {
           val previousCount = binding.tvNumberOfProductCount.text.toString().toInt()
          val updatedCount = previousCount + itemCount
        if (updatedCount > 0){
            binding.llCart.visibility = View.VISIBLE
            binding.tvNumberOfProductCount.text = updatedCount.toString()
        }
        else{

            binding.llCart.visibility = View.GONE
            binding.tvNumberOfProductCount.text = "0"
        }
    }

    override fun savingCartItemCount(itemCount: Int) {
        viewModel.fetchTotalCartItemCount().observe(this){
            viewModel.savingCartItemCount(it+itemCount)
        }
    }

    override fun hideCartLayout() {
        binding.llCart.visibility = View.GONE
        binding.tvNumberOfProductCount.text = "0"
    }
}