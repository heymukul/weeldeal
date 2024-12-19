package com.example.project1

object Constants {
    val MERCHANTID = "PGTESTPAYUAT"
    val SALT_KEY = "099eb0cd-02cf-4e2a-8aca-3e6c6aff0399"
    var apiEndPoint = "/pg/v1/pay"
    val merchantTransactionId = "txnID"
    var allProductCategory = arrayOf(
        "bajaj bike",
        "honda car",
        "mahindra car",
        "nissan car",
        "maruti car",
        "tata car",
        "royal enfiled",
        "toyota"
    )
    val allProductIcon = arrayOf(
        R.drawable.bajaj,
        R.drawable.honda,
        R.drawable.mahindra,
        R.drawable.nissan,
        R.drawable.maruti,
        R.drawable.tata,
        R.drawable.royal_enfiled,
        R.drawable.toyota
    )
}