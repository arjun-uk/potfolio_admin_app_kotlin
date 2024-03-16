package com.admin.portfolio.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.admin.portfolio.databinding.ActivityLoginBinding
import com.admin.portfolio.ui.home.view.ActivityHome
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase

class ActivityLogin : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding;
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        init();
    }
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this,ActivityHome::class.java))
        }
    }
    private fun init(){


        binding.buttonSubmit.setOnClickListener {
            LoginSetup(binding.edtEmail.text.toString(),binding.edtPassword.toString())
        }

    }
    private fun LoginSetup(email:String,password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this,ActivityHome::class.java))
                } else {
                    Log.d("ActivityLogin", "LoginSetup: ",task.exception)

                }
            }
    }
}