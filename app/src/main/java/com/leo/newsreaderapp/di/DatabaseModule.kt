package com.leo.newsreaderapp.di

import android.app.Application
import androidx.room.Room
import com.leo.newsreaderapp.database.ArticleDao
import com.leo.newsreaderapp.database.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application, callback: NewsDatabase.Callback): NewsDatabase {
        return Room.databaseBuilder(application, NewsDatabase::class.java, "news_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideArticleDao(db: NewsDatabase): ArticleDao {
        return db.getArticleDao()
    }
}