package com.amorphteam.ketub.ui.main.tabs.library.database

import androidx.room.Dao
import androidx.room.Query
import com.amorphteam.ketub.ui.main.tabs.library.model.CategoryModel


@Dao
interface BookDatabaseDao {

    @Query("select * from category_list")
    fun getAllBooks(): List<CategoryModel>

    @Query("select * from category_list WHERE cat_name LIKE '%' || :catName || '%' ORDER BY _id ASC")
    fun getAllBooks(catName:String): List<CategoryModel>

    @Query("select * from category_list WHERE cat_name LIKE '%' || :catName || '%' ORDER BY _Id ASC limit :count ")
    fun getAllBooks(catName:String, count:Int): List<CategoryModel>


}