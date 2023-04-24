package com.amorphteam.ketub.ui.main.tabs.library.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Book")
data class BookModel(
    @ColumnInfo(name = "cover") val bookCover: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val bookName: String
    )