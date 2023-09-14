package com.amorphteam.ketub.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amorphteam.ketub.utility.OnlineReference

@Entity(tableName = "sections_list")

class RecommandedTocModel (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id :Int,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "book_path")
    val bookPath: String?,

    @ColumnInfo(name = "book_name")
    val bookName: String?,

    @ColumnInfo(name = "section_index")
    val navIndex: Int?,
)