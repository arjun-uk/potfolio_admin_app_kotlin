package com.admin.portfolio.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.admin.portfolio.databinding.ActivityLoginBinding
import com.admin.portfolio.ui.home.view.ActivityHome
import com.admin.portfolio.ui.login.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class ActivityLogin : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding;
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance();
        init();
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, ActivityHome::class.java))
            finish()
        }
    }

    private fun init() {


        binding.buttonSubmit.setOnClickListener {
            if (binding.edtEmail.text.toString().isEmpty()||binding.edtPassword.text.toString().isEmpty()){
                Toast.makeText(this, "fill all credentials", Toast.LENGTH_SHORT).show()
            }else {
                LoginSetup(
                    binding.edtEmail.text.toString(),
                    binding.edtPassword.text.toString(),
                    "arjun"
                )
            }
        }

    }

    private fun LoginSetup(email: String, password: String, username: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("ActivityLogin", "LoginSetup: "+task.result.user)
                    startActivity(Intent(this,ActivityHome::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "No Account", Toast.LENGTH_SHORT).show()

                }
            }
//        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
//            if (it.isSuccessful) {
//                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
//                val user = Users(userId, username, email)
//                FirebaseDatabase.getInstance().reference.child("users").child(userId).setValue(user)
//            } else {
//                Log.d("ActivityLogin", "LoginSetup: ", it.exception)
//
//            }
//        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}