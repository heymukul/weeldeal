package com.example.project1.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.project1.Utils
import com.example.project1.Models.Users
import com.example.project1.databinding.RegisterScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterScreen : AppCompatActivity() {
    private val binding: RegisterScreenBinding by lazy {
        RegisterScreenBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // initialize firebase
        auth = FirebaseAuth.getInstance()
        binding.Register.setOnClickListener {
            val email = binding.emailId.text.toString()
            val username = binding.username.text.toString()
            val password = binding.Password.text.toString()
            val repeatPassword = binding.repeatPassword.text.toString()
            val user = Users( uid = Utils.getCurrentUserid() , email = email , phoneNumber = null , userAddress = "" )

            if(email.isEmpty() || username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()){
                Toast.makeText(this,"Please fill all details",Toast.LENGTH_SHORT).show()
            }
            else if (password != repeatPassword){
                Toast.makeText(this,"Repeat password must be same",Toast.LENGTH_SHORT).show()
            }
            else{
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this) { task->
                    if (task.isSuccessful){
                        auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener {

                                Toast.makeText(this,"Registration Successful Verify your Email",Toast.LENGTH_SHORT).show()
                                FirebaseDatabase.getInstance().getReference("All User").child("User").child(user.uid!!).setValue(user)
                            }
                            ?.addOnFailureListener{
                                Toast.makeText(this,"Registration Failed : ${task.exception?.message}",Toast.LENGTH_SHORT).show()
                            }
                    }
                    else{
                        Toast.makeText(this,"Registration Failed : ${task.exception?.message}",Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }
        binding.signIn.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }
    }
}