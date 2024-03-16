package com.admin.portfolio.ui.home.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import com.admin.portfolio.R
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.admin.portfolio.databinding.FragmentProfileBinding
import com.admin.portfolio.ui.home.view.ActivityHome
import com.admin.portfolio.ui.login.ActivityLogin
import com.admin.portfolio.utils.CustomProgressDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class FragmentProfile : Fragment() {
    private lateinit var binding: FragmentProfileBinding;
    lateinit var storage: FirebaseStorage
    var ImageUrlProfile: String? = null
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
        binding = FragmentProfileBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    private fun init() {
        auth = FirebaseAuth.getInstance();
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
        binding.logOut.setOnClickListener {
            showLogout("Are you sure to logout ?")
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
    private fun signOut() {
        auth.signOut()
        activity?.let{
            val intent = Intent (it, ActivityLogin::class.java)
            it.startActivity(intent)
        }

    }

    private fun showLogout(title: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_logout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        val body = dialog.findViewById(R.id.tvTitle) as TextView
        body.text = title

        val yesBtn = dialog.findViewById(R.id.btn_yes) as AppCompatButton
        yesBtn.setOnClickListener {
            signOut()
            dialog.dismiss()
        }

        val noBtn = dialog.findViewById(R.id.btn_no) as AppCompatButton
        noBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}