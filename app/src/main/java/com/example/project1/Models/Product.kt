package com.example.project1.Models

data class Product(
    var productRandomId : String? = null,
    var productTitle: String? = null,
    var purchaseYear: Int? = null,
    var productPrice: Int? = null,
    var productCategory: String? = null,
    var productType: String? =null,
    var itemCount: Int? = null,
    var adminUid : String? =null,
    var productImageUris: ArrayList<String?> ?= null,
)
