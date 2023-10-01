package com.amorphteam.ketub.database.recommanded_toc

import androidx.room.Dao
import androidx.room.Query
import com.amorphteam.ketub.model.RecommandedTocModel

@Dao
interface RecommandedTocDao {
    @Query("SELECT * FROM sections_list ORDER BY RANDOM() LIMIT 15")
    fun getAllToc(): List<RecommandedTocModel>
}