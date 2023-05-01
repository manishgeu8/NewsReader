package com.leo.newsreaderapp.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leo.newsreaderapp.util.DateUtil

@kotlinx.parcelize.Parcelize
@Entity(tableName = "article_table")
data class Article(
    var id : Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    @PrimaryKey
    val title: String,
    val url: String?,
    val urlToImage: String?,
    var isSearched : Boolean = false
): Parcelable{
    val formattedPublishedAt : String get() {
        publishedAt?.let {
            return DateUtil.formatDate(publishedAt)
        }
        return ""
    }
}