package com.admin.portfolio.ui.home.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.admin.portfolio.databinding.FragmentProfileBinding
import com.admin.portfolio.utils.CustomProgressDialog
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class FragmentProfile : Fragment() {
    private lateinit var binding: FragmentProfileBinding;
    lateinit var storage: FirebaseStorage
    var ImageUrlProfile: String? = null
    lateinit var progressDialog: CustomProgressDialog


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
        binding = FragmentProfileBinding.inflate(layoutInflater)

        init()
        return binding.root
    }

    private fun init() {
        storage = Firebase.storage
        binding.profileImage.setOnClickListener {
            galleryLauncher.launch("image/*")
        }
        binding.buttonSubmit.setOnClickListener {
            if (validateFields()) {
                if (ImageUrlProfile != null) {
                    UpdateProfile()
                } else {
                    Toast.makeText(activity, "Please Select Image", Toast.LENGTH_SHORT).show()
                }

            }

        }
        getProfile()
    }

    private fun UpdateProfile() {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog.showProgressDialog()
        val db = FirebaseFirestore.getInstance()

        val profileData = hashMapOf(
            "name" to binding.edtName.text.toString(),
            "Title" to binding.edtTitle.text.toString(),
            "aboutMe" to binding.edtAbout.text.toString(),
            "ProfileImage" to ImageUrlProfile,
            "Description" to binding.edtDescription.text.toString(),


            )

        db.collection("Portfolio")
            .document("ProfileData")
            .set(profileData)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(activity, "Updated Successfully", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Log.w("Firestore", "Error writing document", e)

            }
    }

    private fun getProfile() {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog.showProgressDialog()
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Portfolio").document("ProfileData")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    progressDialog.dismiss()
                    Log.d("Firestore", "Document data: ${document.data}")
                    val aboutMe = document.getString("aboutMe")
                    ImageUrlProfile = document.getString("ProfileImage")
                    val Title = document.getString("Title")
                    val name = document.getString("name")
                    val Description = document.getString("Description")
                    binding.edtName.setText(name)
                    binding.edtAbout.setText(aboutMe)
                    binding.edtDescription.setText(Description)
                    binding.edtTitle.setText(Title)
                    Glide.with(this).load(ImageUrlProfile).into(binding.profileImage)

                } else {
                    Log.d("Firestore", "No such document!")
                }
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                Log.d("Firestore", "get failed with ", exception)
            }
    }


    private fun UploadImage(uri: Uri) {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog.showProgressDialog()
        val fileName = getFileNameFromUri(uri)
        val storageRef = storage.reference
        val imagesRef = storageRef.child("images/${fileName}")

        imagesRef.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                imagesRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    ImageUrlProfile = downloadUri.toString()
                    Glide.with(this).load(ImageUrlProfile).into(binding.profileImage)
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


    private fun validateFields(

    ): Boolean {
        return if (binding.edtName.text.toString().isEmpty() || binding.edtTitle.text.toString()
                .isEmpty()
            || binding.edtDescription.text.toString().isEmpty() || binding.edtAbout.text.toString()
                .isEmpty()
        ) {
            Toast.makeText(activity, "All fields are required", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

}