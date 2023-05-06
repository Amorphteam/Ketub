package com.amorphteam.ketub.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_list")
data class CategoryModel(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Int,

    @ColumnInfo(name = "cat_name")
    val catName: String?,

    @ColumnInfo(name = "cat_cover")
    val catCover: String?,

    @ColumnInfo(name = "des")
    val des: String?
)
