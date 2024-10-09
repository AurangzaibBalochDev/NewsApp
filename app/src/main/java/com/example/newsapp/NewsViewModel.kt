package com.example.newsapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kwabenaberko.newsapilib.NewsApiClient
import com.kwabenaberko.newsapilib.models.Article
import com.kwabenaberko.newsapilib.models.request.EverythingRequest
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest
import com.kwabenaberko.newsapilib.models.response.ArticleResponse

class NewsViewModel : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>()
    val article: LiveData<List<Article>> = _articles

    init {
        fetchNewsHeadlines()
    }

    fun fetchNewsHeadlines(category: String = "GENERAL") {

        val newsApiClient = NewsApiClient(Constant.apiKey)
        val request = TopHeadlinesRequest.Builder().category(category).language("en").build()

        newsApiClient.getTopHeadlines(request, object : NewsApiClient.ArticlesResponseCallback {
            override fun onSuccess(response: ArticleResponse?) {
                response?.articles?.let {
                    _articles.postValue(it)
                }
            }
            override fun onFailure(throwable: Throwable?) {
                if (throwable != null){
                    throwable.localizedMessage?.let { Log.i("NewsAPI Response Failed", it) }
                }

            }

        })
    }
    fun fetchEverythingWithQuery(query: String) {

        val newsApiClient = NewsApiClient(Constant.apiKey)
        val request = EverythingRequest.Builder().q(query).language("en").build()

        newsApiClient.getEverything(request, object : NewsApiClient.ArticlesResponseCallback {
            override fun onSuccess(response: ArticleResponse?) {
                response?.articles?.let {
                    _articles.postValue(it)
                }
            }
            override fun onFailure(throwable: Throwable?) {
                if (throwable != null){
                    throwable.localizedMessage?.let { Log.i("NewsAPI Response Failed", it) }
                }

            }

        })
    }
}