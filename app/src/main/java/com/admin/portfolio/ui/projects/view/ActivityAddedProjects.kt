package com.admin.portfolio.ui.projects.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.admin.portfolio.R
import com.admin.portfolio.databinding.ActivityAddedProjectsBinding
import com.admin.portfolio.ui.experience.resposne.ExpModel
import com.admin.portfolio.ui.projects.adapter.AdapterProjects
import com.admin.portfolio.ui.projects.resposne.Model
import com.admin.portfolio.utils.CustomProgressDialog
import com.google.firebase.firestore.FirebaseFirestore

class ActivityAddedProjects : AppCompatActivity(),AdapterProjects.ItemCLickListner {
    private lateinit var binding:ActivityAddedProjectsBinding;
    private lateinit var adapterProjects: AdapterProjects
    private lateinit var progressDialog: CustomProgressDialog
    private val dataList: MutableList<Model> = mutableListOf()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityAddedProjectsBinding.inflate(layoutInflater);
        setContentView(binding.root)

        init()
    }

    private fun init(){
        binding.back.setOnClickListener{finish()}
        ProjectList()


    }

    private fun ProjectList(){
        progressDialog = CustomProgressDialog(this)
        progressDialog.showProgressDialog()
        db.collection("PortfolioProjects")
            .get()
            .addOnSuccessListener { documents ->
                progressDialog.dismiss()
                for (document in documents) {

                    val projectName = document.getString("project_name")
                    val image = document.getString("image")
                    val id =  document.id
                    Log.d("ActivityAddedProjects", "ProjectList: "+id)
                    projectName?.let { projectName ->
                        image?.let { image ->
                            val model = Model(id,projectName, image)
                            dataList.add(model)
                            adapterProjects = AdapterProjects(this,dataList,this)
                            binding.list.setHasFixedSize(true)
                            binding.list.layoutManager = LinearLayoutManager(this)
                            binding.list.adapter = adapterProjects
                        }
                    }
                }
                //adapterProjects.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
               progressDialog.dismiss()
            }
    }

    override fun ItemClick(id: String?, delete: ImageView?, list: List<Model>, position: Int) {
        delete?.setOnClickListener{

            val builder = AlertDialog.Builder(this)
            builder.setTitle("delete")
            builder.setMessage("Are you sure delete this project ?")
            builder.setIcon(android.R.drawable.ic_delete)
            builder.setPositiveButton("Yes"){dialogInterface, which ->
                DeleteProject(list[position])
                adapterProjects.notifyItemRemoved(position);
            }
            builder.setNegativeButton("No"){dialogInterface, which ->

            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()

        }
    }

    private fun DeleteProject(model: Model){
        progressDialog = CustomProgressDialog(this)
        progressDialog.showProgressDialog()
        db.collection("PortfolioProjects")
            .document(model.id)
            .delete()
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
            }
    }
}