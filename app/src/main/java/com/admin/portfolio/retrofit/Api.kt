package com.admin.portfolio.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(Points.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }

}
object ApiClient {
    val apiService: ApiInterface by lazy {
        Api.getInstance().create(ApiInterface::class.java)
    }
}