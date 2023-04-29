package com.amorphteam.ketub.ui.main.tabs.bookmark.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark_list")
class BookmarkModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id :Int,

    @ColumnInfo(name = "book_title")
    val bookTitle: String,

    @ColumnInfo(name = "book_name")
    val bookName: String
)
