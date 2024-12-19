package com.example.project1

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.project1.databinding.ProgressDialogBinding
import com.google.firebase.auth.FirebaseAuth
import java.time.DateTimeException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Utils {
    private var dialog : AlertDialog? = null
    fun showDialog(context: Context, message : String){
        val progress = ProgressDialogBinding.inflate(LayoutInflater.from(context))
        progress.tvMessage.text = message
        dialog = AlertDialog.Builder(context).setView(progress.root).setCancelable(false).create()
        dialog!!.show()
    }
    fun hideDialog(){
        dialog?.dismiss()
    }
    fun getCurrentUserid() : String{
      return FirebaseAuth.getInstance().currentUser!!.uid
    }
    fun showToast(context:Context,message : String){
        Toast.makeText(context,message, Toast.LENGTH_LONG).show()
    }
    fun getRandomId(): String{
        return (1 ..25).map { (('a'..'z') + ('A'..'Z') + ('0'..'9')).random() }.joinToString("")
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String?{
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return currentDate.format(formatter)
    }
}