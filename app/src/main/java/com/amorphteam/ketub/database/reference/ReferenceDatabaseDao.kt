package com.amorphteam.ketub.database.reference

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amorphteam.ketub.model.ReferenceModel
import com.amorphteam.ketub.utility.OnlineReference


@Dao
interface ReferenceDatabaseDao {

    @Query("select * from reference_table")
    fun getAllReferences(): List<ReferenceModel>

    @Query("select * from reference_table  WHERE book_name LIKE '%' || :catName || '%'")
    fun getAllReferences(catName: String): List<ReferenceModel>


    @Query("select * from reference_table  WHERE online_reference = :onlineReference")
    fun getAllReferences(onlineReference: OnlineReference): List<ReferenceModel>

    @Query("select * from reference_table WHERE book_name = :bookName")
    fun getAllReferencesForSingleBook(bookName: String): List<ReferenceModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reference: ReferenceModel)

    @Query("DELETE FROM reference_table WHERE id = :id")
    suspend fun delete(id: Int): Int

    @Query("SELECT * FROM reference_table WHERE book_name = :bookName AND nav_index = :navIndex")
    fun getBookmarkSelected(bookName: String, navIndex: Int): Boolean

}

