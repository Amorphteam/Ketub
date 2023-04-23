package com.amorphteam.ketub.ui.main.tabs.library

import android.app.Application
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.*
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import com.amorphteam.ketub.ui.main.tabs.library.model.MainToc
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.TempData
import java.io.IOException
import java.io.InputStream


class LibraryFragmentViewModel(application: Application) : AndroidViewModel(application) {
    var startEpubAct = MutableLiveData<Boolean>()
    var startSearchAct = MutableLiveData<Boolean>()
    var startDetailFrag = MutableLiveData<Boolean>()
    private val repository: BookRepository
    val allBooks: LiveData<List<BookModel>>


    init {
        Log.i(Keys.LOG_NAME, "main view model created")
            val dao = BookDatabase.getDatabase(application).getBookDao()
            repository = BookRepository(dao)
            allBooks = repository.getAllBooks()
    }



    override fun onCleared() {
        super.onCleared()
        Log.i(Keys.LOG_NAME, "main view model was cleared")
    }

    fun getEjtihadItem(): MutableLiveData<ArrayList<BookModel>> {
        val array = MutableLiveData<ArrayList<BookModel>>()
        array.value = arrayListOf<BookModel>().apply { allBooks }
        return array
    }

    fun getNososItem(): MutableLiveData<ArrayList<BookModel>> {
        val array = MutableLiveData<ArrayList<BookModel>>()
        array.value = TempData.bookArray
        return array
    }


    fun getReadMoreMainToc(): MutableLiveData<ArrayList<MainToc>> {
        val array = MutableLiveData<ArrayList<MainToc>>()
        array.value = TempData.mostRead
        return array
    }

    fun getRecommandedToc(): MutableLiveData<ArrayList<MainToc>> {
        val array = MutableLiveData<ArrayList<MainToc>>()
        array.value = TempData.mostRead
        return array
    }

    fun openEpubAct() {
        startEpubAct.value = true
    }

    fun openDetailFrag() {
        startDetailFrag.value = true
    }

    fun openSearchAct() {
        startSearchAct.value = true
    }
}

