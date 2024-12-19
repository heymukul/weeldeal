package com.example.project1.Models

import com.example.project1.roomdb.CartProduct

data class Orders(
    val orderId: String?=null,
    val orderDate: String?=null,
    val orderList: List<CartProduct>?=null,
    val userAddress: String?=null,
    val orderStatus: Int?=null,
    val orderingUserId: String?=null,
)
