package com.example.project1.viewmodel

import android.adservices.adid.AdId
import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project1.Constants
import com.example.project1.Models.Orders
import com.example.project1.Models.Product
import com.example.project1.Utils
import com.example.project1.api.ApiUtilities
import com.example.project1.roomdb.CartProduct
import com.example.project1.roomdb.CartProductDao
import com.example.project1.roomdb.CartProductsDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow

class UserViewModels(application: Application) : AndroidViewModel(application) {
    //initialization
    val sharedPreferences : SharedPreferences = application.getSharedPreferences("My_pref", MODE_PRIVATE)
    var cartProductDao : CartProductDao = CartProductsDatabase.getDatabaseInstance(application).CartProductDao()
    private val _paymentStatus = MutableStateFlow<Boolean>(false)
    val paymentStatus = _paymentStatus

    //room Db
    suspend fun insertCartProduct(product: CartProduct){
        cartProductDao.insertCartProduct(product)

    }
    suspend fun updateCartProduct(product: CartProduct){
        cartProductDao.updateCartProduct(product)
    }
    fun getAll(): LiveData<List<CartProduct>>{
        return cartProductDao.getAllCartProduct()
    }
    suspend fun  deleteCartProduct(productId : String){
        cartProductDao.deleteCartProduct(productId)
    }

   suspend fun deleteCartProduct(){
        cartProductDao.deleteCartProduct()
    }

    //firebaseCall
    fun fetchAllTheProduct(): Flow<List<Product>> = callbackFlow {
        val db = FirebaseDatabase.getInstance().getReference("Admin").child("AllProduct")
        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = ArrayList<Product>()
                for (product in snapshot.children) {
                    val prod = product.getValue(Product::class.java)

                    products.add((prod!!))

                }
                trySend(products)
            }

            override fun onCancelled(snapshot: DatabaseError) {

            }

        }
        db.addValueEventListener(eventListener)
        awaitClose {
            db.removeEventListener(eventListener)
        }
    }
    fun getOrderProduct(orderId: String): Flow<List<CartProduct>> = callbackFlow{
           val db = FirebaseDatabase.getInstance().getReference("Admin").child("Orders").child(orderId)
        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val order = snapshot.getValue(Orders::class.java)
                trySend(order?.orderList!!)

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        }
        db.addValueEventListener(eventListener)
        awaitClose {
            db.removeEventListener(eventListener)
        }
    }

    fun getAllOrder():Flow<List<Orders>> = callbackFlow {
        val db = FirebaseDatabase.getInstance().getReference("Admin").child("Orders")
            .orderByChild("orderStatus")
        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderList = ArrayList<Orders>()
                for (orders in snapshot.children) {
                    val orderData = orders.getValue(Orders::class.java)
                    if (orderData?.orderingUserId == Utils.getCurrentUserid()) {
                        orderList.add(orderData)
                    }
                }
                trySend(orderList)

            }
            override fun onCancelled(p0: DatabaseError) {

            }
        }
        db.addValueEventListener(eventListener)
        awaitClose {
            db.removeEventListener(eventListener)
        }
    }
            fun getCategoryProduct(category: String): Flow<List<Product>> = callbackFlow {
        val db = FirebaseDatabase.getInstance().getReference("Admin")
            .child("ProductCategory/${category}")

        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = ArrayList<Product>()
                for (product in snapshot.children) {
                    val prod = product.getValue(Product::class.java)

                    products.add((prod!!))

                }
                trySend(products)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        db.addValueEventListener(eventListener)
        awaitClose {
            db.removeEventListener(eventListener)
        }
    }

    fun updateItemCount (product: Product,itemCount: Int){
        FirebaseDatabase.getInstance().getReference("Admin").child("AllProduct/${product.productRandomId}").child("itemCount").setValue(itemCount)
        FirebaseDatabase.getInstance().getReference("Admin").child("ProductCategory/${product.productCategory}/${product.productRandomId}").child("itemCount").setValue(itemCount)
        FirebaseDatabase.getInstance().getReference("Admin").child("ProductType/${product.productType}/${product.productRandomId}").child("itemCount").setValue(itemCount)
    }
   fun saveProductAfterOrder( ItemCount: Int ,product: CartProduct ){
       FirebaseDatabase.getInstance().getReference("Admin").child("AllProduct/${product.productId}").child("itemCount").setValue(0)
       FirebaseDatabase.getInstance().getReference("Admin").child("ProductCategory/${product.productCategory}/${product.productId}").child("itemCount").setValue(0)
      // FirebaseDatabase.getInstance().getReference("Admin").child("ProductType/${product.productType}/${product.productId}").child("itemCount").setValue(0)

   }
    fun saveUserAddress(address: String){
        FirebaseDatabase.getInstance().getReference("All Users").child("Users").child(Utils.getCurrentUserid()).child("userAddress").setValue(address)
    }

    fun getUserAddress(callback:(String?)->Unit){
       val db =  FirebaseDatabase.getInstance().getReference("All Users").child("Users").child(Utils.getCurrentUserid()).child("userAddress")
        db.addListenerForSingleValueEvent(object : ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val address = snapshot.getValue(String::class.java)
                        callback(address)
                    }
                   else{
                       callback(null)
                    }
               }

               override fun onCancelled(error: DatabaseError) {
                   callback(null)
               }

           })
    }

    fun saveOrderedProduct(orders: Orders){
        FirebaseDatabase.getInstance().getReference("Admin").child("Orders").child(orders.orderId!!).setValue(orders)
    }
    //sharedPreferences
    fun savingCartItemCount(itemCount: Int){
        sharedPreferences.edit().putInt("itemCount",itemCount).apply()
    }
    fun fetchTotalCartItemCount() : MutableLiveData<Int>{
        val totalItemCount = MutableLiveData<Int>()
        totalItemCount.value = sharedPreferences.getInt("ItemCount",0)
        return totalItemCount
    }
    fun logoutUser(){
        FirebaseAuth.getInstance().signOut()
    }
//address Check
    fun saveAddressStatus(){
    sharedPreferences.edit().putBoolean("addressStatus",true).apply()
    }
    fun getAddressStatus():MutableLiveData<Boolean>{
      val status = MutableLiveData<Boolean>()
      status.value = sharedPreferences.getBoolean("addressStatus",false)
    return status
    }
    //retrofit
    suspend fun checkPayment(headers : Map<String,String>){
       val res =  ApiUtilities.statusAPi.checkStatus(headers,Constants.MERCHANTID,Constants.merchantTransactionId)
        if(res.body()!= null && res.body()!!.success){
            _paymentStatus.value = true
        }
        else{
            _paymentStatus.value = false
        }
    }

}