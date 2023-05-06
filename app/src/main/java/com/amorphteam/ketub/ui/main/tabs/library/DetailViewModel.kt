package com.amorphteam.ketub.ui.main.tabs.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.database.book.BookRepository
import com.amorphteam.ketub.model.BookModel
import com.amorphteam.ketub.model.CategoryModel
import com.amorphteam.ketub.model.CatSection
import com.amorphteam.ketub.utility.DatabaseBookHelper

class DetailViewModel(private val bookRepository: BookRepository, var catSection: CatSection) :
    ViewModel() {
    private val _startEpubAct = MutableLiveData<Boolean>()
    val startEpubAct: LiveData<Boolean>
        get() = _startEpubAct

    private val _startLibraryFrag = MutableLiveData<Boolean>()
    val startLibraryFrag: LiveData<Boolean>
        get() = _startLibraryFrag

    private var _allCats = MutableLiveData<List<CategoryModel>>()
    val allCats: LiveData<List<CategoryModel>?>
        get() = _allCats

    private var _bookItems = MutableLiveData<List<BookModel>>()
    val bookItems: LiveData<List<BookModel>>
        get() = _bookItems

    private var databaseBookHelper: DatabaseBookHelper? = DatabaseBookHelper.getInstance()

    init {
        databaseBookHelper?.getCats(catSection.title, bookRepository, _allCats)
    }

    fun openLibraryFrag() {
        _startLibraryFrag.value = true
    }

    override fun onCleared() {
        super.onCleared()
        databaseBookHelper = null
    }

    fun openEpubAct() {
        _startEpubAct.value = true
    }

    fun getCatId(id: Int) {
        databaseBookHelper?.getBookItems(id, bookRepository, _bookItems)
    }
}