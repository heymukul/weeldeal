package com.example.project1.Activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project1.CartListener
import com.example.project1.Models.Orders
import com.example.project1.Utils
import com.example.project1.adapter.AdapterCartProducts
import com.example.project1.databinding.ActivityOrderPlaceBinding
import com.example.project1.databinding.AddressLayoutBinding
import com.example.project1.viewmodel.UserViewModels
import com.phonepe.intent.sdk.api.B2BPGRequest
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.security.MessageDigest


class OrderPlaceActivity : AppCompatActivity(), PaymentResultWithDataListener {
    private lateinit var binding: ActivityOrderPlaceBinding
    private val viewModel : UserViewModels by viewModels()
    private lateinit var b2BPGRequest : B2BPGRequest
    private lateinit var adapterCartProducts: AdapterCartProducts
    private var cartListener : CartListener? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getAllCardProduct()
        backToUserActivity()
        onPlaceOrderClicked()
        //initializePhonePay()
    }

    private fun initRazorPay() {
        val activity: Activity = this
        val co = Checkout()
        try {
            val options = JSONObject()
            options.put("name","WheelDeals")
            options.put("description","Demoing Charges")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#FF1515");
            options.put("currency","INR");
            //options.put("order_id", Utils.getRandomId());
            options.put("amount","50000")//pass amount in currency subunits

            val retryObj = JSONObject()
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email","mukulkumar@example.com")
            prefill.put("contact","7895678956")

            options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }


    //    private fun initializePhonePay() {
//        val data = JSONObject()
//        PhonePe.init(this,PhonePeEnvironment.UAT,Constants.MERCHANTID,"")
//        data.put("merchantId",Constants.MERCHANTID)
//        data.put("merchantTransactionId",Constants.merchantTransactionId)
//        data.put("amount", 1000)
//        data.put("mobileNumber","9999999999")
//        data.put("callbackUrl","https://webhook.site/3c1b2ee0-080e-4c05-a3f4-c114eeba7bdd")
//
//        val paymentInstrument = JSONObject()
//        paymentInstrument.put("type","UPI_INTENT")
//        paymentInstrument.put("targetApp","com.phonepe.simulator")
//
//        data.put("paymentInstrument",paymentInstrument)
//
//        val deviceContext = JSONObject()
//        deviceContext.put("deviceOS","ANDROID")
//        data.put("deviceContext",deviceContext)
//
//
//        val payloadBase64 = android.util.Base64.encodeToString(
//            data.toString().toByteArray(Charset.defaultCharset()), android.util.Base64.NO_WRAP
//        )
//
//        val checksum = sha256(payloadBase64 + Constants.apiEndPoint + Constants.SALT_KEY) + "###1"
//        b2BPGRequest = B2BPGRequestBuilder()
//            .setData(payloadBase64)
//            .setChecksum(checksum)
//            .setUrl(apiEndPoint)
//            .build()
//    }
//   private fun sha256(input: String):String{
//       val bytes = input.toByteArray(Charsets.UTF_8)
//       val md = MessageDigest.getInstance("SHA-256")
//       val digest = md.digest(bytes)
//       return digest.fold(""){str,it->str+"%02x".format(it)}
//   }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun onPlaceOrderClicked() {
        binding.btnPlaceOrder.setOnClickListener{
            viewModel.getAddressStatus().observe(this){status ->
                if (status){
                       //getPaymentView()
                    val co = Checkout()
                    // apart from setting it in AndroidManifest.xml, keyId can also be set
                    // programmatically during runtime
                    co.setKeyID("rzp_test_uPCGzJmMpfiVq2")
                    initRazorPay()
                }
               else{
                 val addressLayoutBinding = AddressLayoutBinding.inflate(LayoutInflater.from(this))
                    val alertDialog = AlertDialog.Builder(this)
                        .setView(addressLayoutBinding.root)
                        .create()
                    alertDialog.show()

                    addressLayoutBinding.btnAdd.setOnClickListener{
                        saveAddress(alertDialog,addressLayoutBinding)
                    }
                }
            }
        }
    }
   @RequiresApi(Build.VERSION_CODES.O)
//   private val phonePayView = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//       if (it.resultCode== RESULT_OK){
//           checkStatus()
//       }
//   }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun checkStatus() {
//      val xVerify = sha256("/pg/v1/status/${Constants.MERCHANTID}/${Constants.merchantTransactionId}${Constants.SALT_KEY}") + "###1"
//        val headers = mapOf(
//            "Content-Type" to "application/json",
//            "X-Verify" to xVerify,
//            "X-MERCHANT-ID" to Constants.MERCHANTID
//
//        )
//        lifecycleScope.launch {
//            viewModel.checkPayment(headers)
//            viewModel.paymentStatus.collect{status ->
//                if (status){
//                    Utils.showToast(this@OrderPlaceActivity,"Payment done")
//                    saveOrder()
//                    viewModel.deleteCartProduct()
//                    viewModel.savingCartItemCount(0)
//                    cartListener?.hideCartLayout()
//
//                    Utils.hideDialog()
//                    startActivity(Intent(this@OrderPlaceActivity , UserMainActivity::class.java))
//                    finish()
//                }
//                else{
//                    Utils.showToast(this@OrderPlaceActivity,"Payment  not done")
//                }
//            }
//        }
//
//    }
   // @RequiresApi(Build.VERSION_CODES.O)
    private fun saveOrder() {
        viewModel.getAll().observe(this){cartProductList ->

            if (cartProductList.isEmpty()){
                viewModel.getUserAddress { address ->
                    val order = Orders(
                        orderId = Utils.getRandomId(),
                        userAddress = address, orderStatus = 0, orderDate = Utils.getCurrentDate(),
                        orderingUserId = Utils.getCurrentUserid()
                    )
                    viewModel.saveOrderedProduct(order)
                }
            }
           //viewModel.saveProductAfterOrder(0, product)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
//    private fun getPaymentView() {
//        try {
//            PhonePe.getImplicitIntent(this,b2BPGRequest,"com.phonepe.simulator")
//                .let {
//                     phonePayView.launch(it)
//                }
//        }
//        catch (e:PhonePeInitException){
//            Utils.showToast(this,e.message.toString())
//        }
//    }

    //@RequiresApi(Build.VERSION_CODES.O)
    private fun saveAddress(alertDialog: AlertDialog, addressLayoutBinding: AddressLayoutBinding) {
        Utils.showDialog(this,"processing...")
        val userPinCode = addressLayoutBinding.etPinCode.text.toString()
        val userPhoneNumber = addressLayoutBinding.etPhoneNo.text.toString()
        val userState = addressLayoutBinding.etState.text.toString()
        val userDistrict = addressLayoutBinding.etDistrict.text.toString()
        val userAddress = addressLayoutBinding.etDescriptiveAddress.text.toString()

        val address = "$userPinCode,$userDistrict($userState),$userAddress,$userPhoneNumber"


        lifecycleScope.launch{
            viewModel.saveUserAddress(address)
            viewModel.saveAddressStatus()
        }
        Utils.showToast(this,"Saved")
        alertDialog.dismiss()
        initRazorPay()
        //getPaymentView()
    }

    private fun backToUserActivity() {
        binding.tbOrderFragment.setNavigationOnClickListener{
            startActivity(Intent(this,UserMainActivity::class.java))
            finish()
        }
    }

    private fun getAllCardProduct() {
            viewModel.getAll().observe(this){ cartProductList ->
                adapterCartProducts = AdapterCartProducts()
                binding.rvProductItems.adapter = adapterCartProducts
                adapterCartProducts.differ.submitList(cartProductList)

                var totalPrice = 0
                for (product in cartProductList){
                    val price = product.productPrice?.substring(1)?.toInt()
                    val itemCount = product.productCount!!
                    totalPrice +=(price?. times(itemCount)!!)

                }
                binding.tvSubTotal.text = totalPrice.toString()

                if (totalPrice > 200){
                    binding.tvDeliveryCharge.text = "₹1500"
                    totalPrice += 1500
                }
                binding.tvGrandTotal.text = totalPrice.toString()
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        Utils.showToast(this , "Payment Success")

        lifecycleScope.launch {
            saveOrder()
            viewModel.deleteCartProduct()
            viewModel.savingCartItemCount(0)
            cartListener?.hideCartLayout()
            Utils.hideDialog()
            startActivity(Intent(this@OrderPlaceActivity , UserMainActivity::class.java))
            finish()
        }

    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Utils.showToast(this , "Payment Failed${p1}")
    }
}
