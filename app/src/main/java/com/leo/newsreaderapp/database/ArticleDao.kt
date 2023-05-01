package com.leo.newsreaderapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article_table")
    fun getArticles() : LiveData<List<Article>>

    @Query("SELECT * FROM article_table WHERE isSearched = true")
    fun getSearchedArticles() : LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(article: Article) : Long

    @Query("DELETE FROM article_table WHERE isSearched = true")
    fun deleteAllSearchedArticles()
}