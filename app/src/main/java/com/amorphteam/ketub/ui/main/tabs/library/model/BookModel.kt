package com.amorphteam.ketub.ui.main.tabs.library.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book")
data class BookModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id") val id: Int?,
    @ColumnInfo(name = "Cover") val bookCover: Int,
    @ColumnInfo(name = "Name") val bookName: String
    )