package com.admin.portfolio.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.admin.portfolio.R
import com.admin.portfolio.databinding.ActivitySplashBinding
import com.admin.portfolio.ui.home.view.ActivityHome
import com.admin.portfolio.ui.login.ActivityLogin

class ActivitySplash : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater);
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ActivityLogin::class.java)
            startActivity(intent)
            finish()
        }, 3000)


    }
}