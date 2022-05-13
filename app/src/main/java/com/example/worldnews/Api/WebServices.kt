package com.example.worldnews.Api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {

    @GET("sources")
    fun getSources(): Call<ResourcesResponse>

    @GET("everything")
    fun getArticles(@Query("sources") sources: String): Call<ArticlesResponse>
}