package com.admin.portfolio.libs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.admin.portfolio.databinding.ItemProjectsBinding
import com.bumptech.glide.Glide

class AdapterQuotes(val context: Context, var list: List<Movie>) : RecyclerView.Adapter<AdapterQuotes.MyViewHolder>() {
    private lateinit var binding: ItemProjectsBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterQuotes.MyViewHolder {
        binding = ItemProjectsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterQuotes.MyViewHolder, position: Int) {
        binding.title.text = list[holder.adapterPosition].title
        Glide.with(context).load(list[holder.adapterPosition].movie_image).into(binding.image)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(binding: ItemProjectsBinding): RecyclerView.ViewHolder(binding.root)

}