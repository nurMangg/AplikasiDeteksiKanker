package com.dicoding.asclepius.repo

import android.annotation.SuppressLint
import com.dicoding.asclepius.api.ApiClient
import com.dicoding.asclepius.api.ApiConfig
import com.dicoding.asclepius.database.News
import com.dicoding.asclepius.database.NewsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsRepository {
    @SuppressLint("SimpleDateFormat")
    fun getHealthNews(
        onSuccess: (List<NewsItem>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        ApiClient.newsApiService.searchHealthNews("cancer", "health", "en", ApiConfig.KEY)
            .enqueue(object : Callback<News> {
                override fun onResponse(call: Call<News>, response: Response<News>) {
                    if (response.isSuccessful) {
                        val news = response.body()?.articles ?: emptyList()
                        val newsList = news.mapNotNull { new ->
                            if (new.title.isNotEmpty() && !new.urlToImage.isNullOrEmpty() && new.url.isNotEmpty() && !new.description.isNullOrEmpty()) {
                                NewsItem(new.title, new.urlToImage, new.url, new.description)
                            } else {
                                null
                            }
                        }
                        onSuccess(newsList)
                    } else {
                        onFailure("Failed to fetch news")

                    }
                }

                override fun onFailure(call: Call<News>, t: Throwable) {
                    onFailure(t.message ?: "Unknown error")
                }
            })
    }

}