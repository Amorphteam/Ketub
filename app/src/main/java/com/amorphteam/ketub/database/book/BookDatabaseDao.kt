package com.amorphteam.ketub.database.book

import androidx.room.Dao
import androidx.room.Query
import com.amorphteam.ketub.model.BookModel
import com.amorphteam.ketub.model.CategoryModel


@Dao
interface BookDatabaseDao {
    @Query("select * from category_list")
    fun getAllCats(): List<CategoryModel>

    @Query("select * from category_list WHERE _id is :catId")
    fun getCat(catId:Int): List<CategoryModel>

    @Query("select * from category_list WHERE cat_name LIKE '%' || :catName || '%' ORDER BY _id ASC")
    fun getAllCats(catName:String): List<CategoryModel>

    @Query("select * from category_list WHERE cat_name LIKE '%' || :catName || '%' ORDER BY _Id ASC limit :count ")
    fun getAllCats(catName:String, count:Int): List<CategoryModel>

    @Query("SELECT * FROM book_list WHERE cat_id IN (:catIds)")
    fun getAllBooks(catIds: List<Int>): List<BookModel>

    @Query("select * from book_list WHERE cat_id = :catId")
    fun getAllBooks(catId:Int): List<BookModel>


    @Query("select * from book_list")
    fun getAllBooks(): List<BookModel>

    @Query("select cat_id from book_list WHERE book_path = :bookName")
    fun getBook(bookName:String): Int
}
