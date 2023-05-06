package com.amorphteam.ketub.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.OnlineReference

@Entity(tableName = "reference_table")
class ReferenceModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id :Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "book_name")
    val bookName: String,

    @ColumnInfo(name = "book_path")
    val bookPath: String,

    @ColumnInfo(name = "nav_index")
    val navIndex: Int?,

    @ColumnInfo(name = "nav_uri")
    val navUri: String?,

    @ColumnInfo(name = "scroll_percent")
    val scrollPercent: Float?,

    @ColumnInfo(name = "online_reference")
    var onlineReference: OnlineReference = OnlineReference.NONE


)
