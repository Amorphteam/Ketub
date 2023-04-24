package com.amorphteam.ketub.ui.main.tabs.library.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Book")
data class BookModel(
    @PrimaryKey(autoGenerate = true)
    val id :Int,

    @ColumnInfo(name = "cover")
    val bookCover: String,

    @ColumnInfo(name = "name")
    val bookName: String
    )