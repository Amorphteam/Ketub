package com.amorphteam.ketub.database.recommanded_toc

import androidx.room.Dao
import androidx.room.Query
import com.amorphteam.ketub.model.RecommandedTocModel

@Dao
interface RecommandedTocDao {
    @Query("select * from sections_list")
    fun getAllToc(): List<RecommandedTocModel>
}