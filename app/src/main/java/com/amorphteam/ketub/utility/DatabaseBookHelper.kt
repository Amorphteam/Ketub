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

    fun getAllBooks(bookRepository: BookRepository, bookItems:MutableLiveData<List<BookModel>>) {
        uiScope.launch {
            bookItems.value = getAllBooks(bookRepository)
        }
    }

    fun getBookId(bookPath:String, repository: BookRepository, catId:MutableLiveData<Int>){
        uiScope.launch {
            catId.value = getBookIdFromDB(bookPath, repository)
        }
    }

    fun getBook(bookPath:String, repository: BookRepository, bookList:MutableLiveData<List<BookModel>>){
        uiScope.launch {
            bookList.value = getBookFromDB(bookPath, repository)
        }
    }

    private suspend fun getBookFromDB(bookPath: String, repository: BookRepository): List<BookModel> {
        return withContext(Dispatchers.IO) {
            val book = repository.getBook(bookPath)
            book
        }
    }

    private suspend fun getBookIdFromDB(bookPath: String, repository: BookRepository): Int {
        return withContext(Dispatchers.IO) {
            val catId = repository.getBookId(bookPath)
            catId
        }
    }

    fun getCats(catSectionTitle:String, repository: BookRepository, allCatArray: MutableLiveData<List<CategoryModel>>) {
        uiScope.launch {
            allCatArray.value = getAllCats(catSectionTitle, repository)
        }
    }

    fun getCat(catId:Int, repository: BookRepository, allCatArray: MutableLiveData<List<CategoryModel>>) {
        uiScope.launch {
            allCatArray.value = getCat(catId, repository)
        }
    }

    private suspend fun getCat(catId: Int, repository: BookRepository): List<CategoryModel> {
        return withContext(Dispatchers.IO){
            val cat = repository.getCat(catId)
            cat
        }
    }


    private suspend fun getAllBooks(id: Int, repository: BookRepository): List<BookModel> {
        return withContext(Dispatchers.IO) {
            val book = repository.getAllBooks(id)
            book
        }

    }

    private suspend fun getAllBooks(repository: BookRepository): List<BookModel> {
        return withContext(Dispatchers.IO) {
            val book = repository.getAllBooks()
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