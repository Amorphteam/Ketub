package com.amorphteam.ketub.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_list")
data class BookModel(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Int,

    @ColumnInfo(name = "cat_id")
    val catId: Int,

    @ColumnInfo(name = "book_path")
    val bookPath: String?,

    @ColumnInfo(name = "book_name")
    val bookName: String?,

    @ColumnInfo(name = "book_author")
    val bookAuthor: String?,

    @ColumnInfo(name = "book_cover")
    val bookCover: String?,

    @ColumnInfo(name = "book_style")
    val bookStyle: String?,

    @ColumnInfo(name = "des")
    val des: String?

)