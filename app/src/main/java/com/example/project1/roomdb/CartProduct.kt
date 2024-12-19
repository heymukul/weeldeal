package com.example.project1.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CartProducts")
data class CartProduct (
    @PrimaryKey
    val productId: String = "random",
    var productTitle: String ? = null,
    var productPurchase : Int? = null,
    var productPrice: String? = null,
    var productCount: Int? = null,
    var productImage: String? = null,
    var productCategory: String? = null,
    var adminUid: String? = null,
)