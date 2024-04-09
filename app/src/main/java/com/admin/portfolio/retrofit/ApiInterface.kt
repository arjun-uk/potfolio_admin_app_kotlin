package com.admin.portfolio.retrofit

import com.admin.portfolio.libs.MoviesListResponse
import com.admin.portfolio.libs.QuoteList
import com.admin.portfolio.ui.experience.resposne.ExpModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiInterface {
    @POST("trending_movies")
    suspend fun getQuotes() : Response<MoviesListResponse>
}