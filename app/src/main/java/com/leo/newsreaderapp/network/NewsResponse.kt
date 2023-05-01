package com.leo.newsreaderapp.network

import com.leo.newsreaderapp.database.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)