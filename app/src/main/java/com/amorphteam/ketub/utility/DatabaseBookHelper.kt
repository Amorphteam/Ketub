package com.amorphteam.ketub.utility

import androidx.lifecycle.MutableLiveData
import com.amorphteam.ketub.database.book.BookRepository
import com.amorphteam.ketub.model.BookModel
import com.amorphteam.ketub.model.CategoryModel
import kotlinx.coroutines.*


class DatabaseBookHelper {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

     suspend fun getAllCats(catName: String, count: Int, repository: BookRepository): List<CategoryModel> {
        return withContext(Dispatchers.IO) {
            val category = repository.getAllCats(catName, count)
            category
        }
    }

    private suspend fun getAllCats(catName:String, repository: BookRepository): List<CategoryModel> {
        return withContext(Dispatchers.IO) {
            val category = repository.getAllCats(catName)
            category
        }
    }

    fun getBookItems(id: Int, bookRepository: BookRepository, bookItems:MutableLiveData<List<BookModel>>) {
        uiScope.launch {
            bookItems.value = getAllBooks(id, bookRepository)
        }
    }

    fun getCats(catSectionTitle:String, repository: BookRepository, allCatArray: MutableLiveData<List<CategoryModel>>) {
        uiScope.launch {
            allCatArray.value = getAllCats(catSectionTitle, repository)
        }
    }


    private suspend fun getAllBooks(id: Int, repository: BookRepository): List<BookModel> {
        return withContext(Dispatchers.IO) {
            val book = repository.getAllBooks(id)
            book
        }

    }


    companion object {
        private var instance: DatabaseBookHelper? = null

        fun getInstance(): DatabaseBookHelper {
            if (instance == null) {
                instance = DatabaseBookHelper()
            }
            return instance!!
        }
    }
}