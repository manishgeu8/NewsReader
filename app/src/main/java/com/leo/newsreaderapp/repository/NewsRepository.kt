package com.leo.newsreaderapp.repository

import com.leo.newsreaderapp.database.Article
import com.leo.newsreaderapp.database.NewsDatabase
import com.leo.newsreaderapp.network.NewsApi
import com.leo.newsreaderapp.network.NewsResponse
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val newsDatabase: NewsDatabase
) {

    suspend fun getNews(): Response<NewsResponse> {
        return newsApi.getNews()
    }

    suspend fun searchNews(searchQuery: String): Response<NewsResponse> {
        return newsApi.searchForNews(searchQuery)
    }

    fun getAllArticles() = newsDatabase.getArticleDao().getArticles()

    fun getAllSearchedArticles() = newsDatabase.getArticleDao().getSearchedArticles()

    fun deleteAllSearchedArticles() = newsDatabase.getArticleDao().deleteAllSearchedArticles()

    suspend fun insertArticle(article: Article) = newsDatabase.getArticleDao().insert(article)

}