package com.admin.portfolio.ui.experience.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.admin.portfolio.R
import com.admin.portfolio.databinding.ActivityExpertiseBinding
import com.admin.portfolio.ui.experience.adapter.AdapterExperience
import com.admin.portfolio.ui.experience.resposne.ExpModel
import com.admin.portfolio.ui.projects.adapter.AdapterProjects
import com.admin.portfolio.ui.projects.resposne.Model
import com.admin.portfolio.utils.CustomProgressDialog
import com.google.firebase.firestore.FirebaseFirestore

class ActivityExpertise : AppCompatActivity(),AdapterExperience.ItemCLickListner {
    private lateinit var binding:ActivityExpertiseBinding
    private val dataList: MutableList<ExpModel> = mutableListOf()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var adapterExperience: AdapterExperience
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpertiseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    private fun init(){


        binding.back.setOnClickListener{finish()}
        ExpertiseList()

    }

    private fun ExpertiseList(){
        progressDialog = CustomProgressDialog(this)
        progressDialog.showProgressDialog()
        db.collection("PortfolioExpertise")
            .get()
            .addOnSuccessListener { documents ->
                progressDialog.dismiss()
                for (document in documents) {

                    val expertise = document.getString("expertise")
                    val image = document.getString("image")
                    val id =  document.id
                    Log.d("ActivityAddedProjects", "ProjectList: "+id)
                    expertise?.let { expertise ->
                        image?.let { image ->
                            val model = ExpModel(id,expertise, image)
                            dataList.add(model)
                            adapterExperience = AdapterExperience(this,dataList,this)
                            binding.list.setHasFixedSize(true)
                            binding.list.layoutManager = LinearLayoutManager(this)
                            binding.list.adapter = adapterExperience
                        }
                    }
                }
                //adapterExperience.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
            }
    }

    override fun ItemClick(id: String?, delete: ImageView?, list: List<ExpModel>, position: Int) {

        delete?.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("delete")
            builder.setMessage("Are you sure delete this project ?")
            builder.setIcon(android.R.drawable.ic_delete)
            builder.setPositiveButton("Yes"){dialogInterface, which ->
                DeleteExpertise(list[position])
                adapterExperience.notifyItemRemoved(position);
            }
            builder.setNegativeButton("No"){dialogInterface, which ->

            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()

        }

    }

    private fun DeleteExpertise(model: ExpModel){
        progressDialog = CustomProgressDialog(this)
        progressDialog.showProgressDialog()
        db.collection("PortfolioExpertise")
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