package com.example.project1.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CartProductDao {
@Insert(onConflict = OnConflictStrategy.REPLACE)
fun insertCartProduct(product: CartProduct)

@Update
fun updateCartProduct(product: CartProduct)

@Query("SELECT * FROM CartProducts")
fun getAllCartProduct(): LiveData<List<CartProduct>>

@Query("DELETE FROM CartProducts WHERE productId = :productId")
fun  deleteCartProduct(productId : String)

@Query("DELETE FROM CartProducts")
suspend fun deleteCartProduct()

}