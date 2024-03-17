package com.admin.portfolio.ui.home.fragments


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.admin.portfolio.databinding.FragmentExperienceBinding
import com.admin.portfolio.ui.experience.view.ActivityExpertise
import com.admin.portfolio.utils.CustomProgressDialog
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage


class FragmentExperience : Fragment() {
    private lateinit var binding: FragmentExperienceBinding;
    lateinit var storage: FirebaseStorage
    var ImageUrlExp: String? = null
    lateinit var progressDialog: CustomProgressDialog
    private lateinit var auth: FirebaseAuth

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { galleryUri ->
                try {
                    UploadImage(galleryUri)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExperienceBinding.inflate(layoutInflater);
        init()
        return binding.root
    }

    private fun init() {


        storage = Firebase.storage
        binding.displayImg.setOnClickListener {
            galleryLauncher.launch("image/*")
        }
        binding.buttonSubmit.setOnClickListener {
            if (!binding.edtName.text.toString().isEmpty()) {
                if (ImageUrlExp != null) {
                    AddExperience()
                } else {
                    Toast.makeText(activity, "Select Image", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, "Enter Name", Toast.LENGTH_SHORT).show()
            }
        }

        binding.viewAddedList.setOnClickListener {
            startActivity(Intent(activity, ActivityExpertise::class.java))
        }

    }

    private fun AddExperience() {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog.showProgressDialog()
        val db = FirebaseFirestore.getInstance()

        val expData = hashMapOf(
            "user_id" to "UTdbegzrVKXKpgjY0EA9jvy7Qop1",
            "expertise" to binding.edtName.text.toString(),
            "image" to ImageUrlExp,
        )

        db.collection("PortfolioExpertise")
            .add(expData)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(activity, "Created Successfully", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Log.w("Firestore", "Error writing document", e)

            }
    }

    private fun UploadImage(uri: Uri) {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog.showProgressDialog()
        val fileName = getFileNameFromUri(uri)
        val storageRef = storage.reference
        val imagesRef = storageRef.child("expertise_images/${fileName}")

        imagesRef.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                imagesRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    ImageUrlExp = downloadUri.toString()
                    Glide.with(this).load(ImageUrlExp).into(binding.displayImg)
                    progressDialog.dismiss()
                }.addOnFailureListener { e ->
                    progressDialog.dismiss()
                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
            }


    }

    fun getFileNameFromUri(uri: Uri): String? {
        return uri.lastPathSegment
    }


}