package com.admin.portfolio.ui.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.admin.portfolio.R
import com.admin.portfolio.databinding.FragmentExperienceBinding


class FragmentExperience : Fragment() {
    private lateinit var binding: FragmentExperienceBinding;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExperienceBinding.inflate(layoutInflater);
        init()
        return binding.root
    }

    private fun init(){

    }


}