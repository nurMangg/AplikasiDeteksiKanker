package com.dicoding.asclepius.api

import com.dicoding.asclepius.database.NewsDao
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val newsApiService: NewsDao = retrofit.create(NewsDao::class.java)
}