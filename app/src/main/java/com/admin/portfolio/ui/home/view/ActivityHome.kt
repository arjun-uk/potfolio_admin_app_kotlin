package com.admin.portfolio.ui.home.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.admin.portfolio.R
import com.admin.portfolio.databinding.ActivityHomeBinding
import com.admin.portfolio.ui.home.fragments.FragmentExperience
import com.admin.portfolio.ui.home.fragments.FragmentProfile
import com.admin.portfolio.ui.home.fragments.FragmentProjects

class ActivityHome : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }


    private fun init(){
        replaceFragment(FragmentProfile())

        binding.profileTab.setOnClickListener {
            binding.profileTab.background = ContextCompat.getDrawable(this, R.drawable.tab_border_selected)

            binding.experienceTabTab.background = null
            binding.projectTab.background = null
            replaceFragment(FragmentProfile())
        }
        binding.projectTab.setOnClickListener {
            binding.projectTab.background = ContextCompat.getDrawable(this, R.drawable.tab_border_selected)
            binding.experienceTabTab.background = null
            binding.profileTab.background = null
            replaceFragment(FragmentProjects())
        }
        binding.experienceTabTab.setOnClickListener {
            binding.experienceTabTab.background = ContextCompat.getDrawable(this, R.drawable.tab_border_selected)
            binding.projectTab.background = null
            binding.profileTab.background = null
            replaceFragment(FragmentExperience())
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }
}