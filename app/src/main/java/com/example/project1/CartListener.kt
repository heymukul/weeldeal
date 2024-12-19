package com.example.project1

interface CartListener {
   fun showCartLayout(itemCount : Int)

   fun savingCartItemCount(itemCount : Int)

   fun hideCartLayout()
}