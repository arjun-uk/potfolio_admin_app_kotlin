package com.admin.portfolio.libs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.admin.portfolio.databinding.ActivityCropBinding
import com.admin.portfolio.retrofit.ApiClient
import com.admin.portfolio.ui.experience.resposne.ExpModel
import com.admin.portfolio.utils.CustomProgressDialog
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityCrop : AppCompatActivity() {
    private lateinit var binding:ActivityCropBinding
    private lateinit var adapterQuotes: AdapterQuotes
    private lateinit var progressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCropBinding.inflate(layoutInflater)
        setContentView(binding.root)


        listQuotes()

    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun listQuotes(){
        progressDialog = CustomProgressDialog(this)
        progressDialog.showProgressDialog()
        GlobalScope.launch(Dispatchers.Main) {
            val response = ApiClient.apiService.getQuotes();
            if (response.isSuccessful){
                progressDialog.dismiss()
                Log.d("TAG", Gson().toJson(response.body()?.movie_list))
                if (response.body()!!.status == "1"){
                    adapterQuotes = AdapterQuotes(this@ActivityCrop,response.body()!!.movie_list)
                    binding.list.setHasFixedSize(true)
                    binding.list.layoutManager = LinearLayoutManager(this@ActivityCrop)
                    binding.list.adapter=adapterQuotes
                }
            }else{
                progressDialog.dismiss()
            }
        }

    }

}