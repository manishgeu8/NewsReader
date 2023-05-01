package com.leo.newsreaderapp.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.leo.newsreaderapp.util.getOrAwaitValue
import com.leo.newsreaderapp.database.Article
import com.leo.newsreaderapp.database.ArticleDao
import com.leo.newsreaderapp.database.NewsDatabase
import com.leo.newsreaderapp.database.Source
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ArticleDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var articleDatabase: NewsDatabase
    private lateinit var articleDao: ArticleDao

    @Before
    fun setup() {
        articleDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NewsDatabase::class.java
        ).allowMainThreadQueries().build()

        articleDao = articleDatabase.getArticleDao()

        //Add default value
        val source = Source("sourceName", "sourceName")
        val articleToAdd = Article(
            1,
            "Author",
            "Content",
            "Description",
            "Publishead At",
            source,
            "Title",
            "Url",
            "UrlToImage"
        )
        runBlockingTest {
            articleDao.insert(articleToAdd)
        }
    }

    @After
    fun teardown() {
        articleDatabase.close()
    }

    @Test
    fun getAllArticles() = runBlockingTest {
        val articleList = articleDao.getArticles().getOrAwaitValue()
        assertThat(articleList).isNotEmpty()
    }

    @Test
    fun insertArticle() = runBlockingTest {
        val source = Source("sourceName", "sourceName")
        val articleToAdd = Article(
            1,
            "Author",
            "Content",
            "Description",
            "Publishead At",
            source,
            "Title",
            "Url",
            "UrlToImage"
        )
        articleDao.insert(articleToAdd)

        val articleList = articleDao.getArticles().getOrAwaitValue()
        assertThat(articleList).contains(articleToAdd)
    }
}