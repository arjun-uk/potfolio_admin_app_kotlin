package com.admin.portfolio.ui.projects.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.admin.portfolio.databinding.ItemProjectsBinding
import com.admin.portfolio.ui.experience.resposne.ExpModel
import com.admin.portfolio.ui.projects.resposne.Model
import com.bumptech.glide.Glide

class AdapterProjects(val context: Context,val list:List<Model>, var itemCLickListner: ItemCLickListner) : RecyclerView.Adapter<AdapterProjects.MyViewHolder>() {
    lateinit var binding:ItemProjectsBinding


    interface ItemCLickListner {
        fun ItemClick(
            id: String?,
            delete:ImageView?,
            list:List<Model>,
            position: Int
        )
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterProjects.MyViewHolder {
        binding = ItemProjectsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: AdapterProjects.MyViewHolder, position: Int) {
        binding.title.text = list[holder.adapterPosition].name
        Glide.with(context).load(list[holder.adapterPosition].image).into(binding.image)
        itemCLickListner.ItemClick(list[holder.adapterPosition].id,binding.deleteBtn,list,holder.adapterPosition)



    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(binding:ItemProjectsBinding) : RecyclerView.ViewHolder(binding.root)


}