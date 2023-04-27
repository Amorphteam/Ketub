package com.amorphteam.ketub.ui.main.tabs.library.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_list")
data class BookModel(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Int?,

    @ColumnInfo(name = "category_name")
    val bookName: String,

    @ColumnInfo(name = "cat_cover")
    val bookCover: String,

    @ColumnInfo(name = "cat_field1")
    val catField1: String?,

    @ColumnInfo(name = "cat_field2")
    val catField2: String?,

    @ColumnInfo(name = "cat_field3")
    val catField3: String?,

    @ColumnInfo(name = "cat_field4")
    val catField4: String?,

    @ColumnInfo(name = "cat_field5")
    val catField5: String?

)