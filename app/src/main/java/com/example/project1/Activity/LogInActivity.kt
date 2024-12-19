package com.example.project1.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.project1.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onStart() {
        super.onStart()
        val currentUser1: Boolean? = auth.currentUser?.isEmailVerified
        if (currentUser1 == true){
            startActivity(Intent(this, UserMainActivity::class.java))
            finish()
        }
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.logIn.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.Password.text.toString()
            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this,"Please fill all details", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener { task->
                        if (task.isSuccessful){
                            val verification = auth.currentUser?.isEmailVerified
                            if(verification == true) {
                                Toast.makeText(this, "Sign-In Successful", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, UserMainActivity::class.java))
                                finish()
                            }
                            else{

                                Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            Toast.makeText(this,"Sign-In Failed : ${task.exception?.message}",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        binding.Signup.setOnClickListener {
            startActivity(Intent(this, RegisterScreen::class.java))
            finish()
        }
    }
}