package com.leo.newsreaderapp.ui.news

import androidx.lifecycle.viewModelScope
import com.leo.newsreaderapp.base.BaseViewModel
import com.leo.newsreaderapp.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : BaseViewModel() {

    init {
        getNews()
    }

    fun getAllArticles() = newsRepository.getAllArticles()

    fun getAllSearchedArticles() = newsRepository.getAllSearchedArticles()

    fun deleteAllSearchedArticles() {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.deleteAllSearchedArticles()
        }
    }

    fun getSearchedArticles(searchText: String) = viewModelScope.launch {
        runCatching {
            loading()
            deleteAllSearchedArticles()
            val response = newsRepository.searchNews(searchText)
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    resultResponse.articles.map {
                        newsRepository.insertArticle(it.copy(isSearched = true))
                    }
                    loaded()
                } ?: error()
            } else error()
        }.getOrElse {
            error()
        }
    }

    private fun getNews() = viewModelScope.launch {
        runCatching {
            loading()
            val response = newsRepository.getNews()
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    resultResponse.articles.map {
                        newsRepository.insertArticle(it)
                    }
                    loaded()
                } ?: error()
            } else error()
        }.getOrElse {
            error()
        }
    }
}