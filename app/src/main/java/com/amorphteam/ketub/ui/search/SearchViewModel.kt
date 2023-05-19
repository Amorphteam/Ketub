package com.amorphteam.ketub.ui.search

import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.database.book.BookRepository
import com.amorphteam.ketub.model.BookModel
import com.amorphteam.ketub.model.SearchInfoHolder
import com.amorphteam.ketub.model.SearchModel
import com.amorphteam.ketub.utility.DatabaseBookHelper
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.TempData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchViewModel(val bookRepository: BookRepository) : ViewModel() {
    private var databaseBookHelper: DatabaseBookHelper? = DatabaseBookHelper.getInstance()
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val _startEpubAct = MutableLiveData<Boolean>()
    val startEpubAct: LiveData<Boolean>
        get() = _startEpubAct

    private val _allBooks = MutableLiveData<List<BookModel>>()
    val allBooks: LiveData<List<BookModel>>
        get() = _allBooks


    private val _results = MutableLiveData<List<SearchInfoHolder>>()
    val results: LiveData<List<SearchInfoHolder>>
        get() = _results
    init {
        getAllBooks()
    }

    private fun getAllBooks() {
        databaseBookHelper?.getAllBooks(bookRepository, _allBooks)
    }


    override fun onCleared() {
        super.onCleared()
        databaseBookHelper = null
        viewModelJob.cancel()
    }


    fun openEpubAct() {
        _startEpubAct.value = true
    }

    fun searchAllBooks(searchHelper: SearchHelper, it: List<String>, s: String) {
        uiScope.launch{
            searchHelper.searchAllBooks(it, s).collect{
                _results.value = it
            }

        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("searchResult")
        fun TextView.setSearchResult(item: SearchInfoHolder?) {
            item?.let {
                text = item.spanna
            }
        }

        @JvmStatic
        @BindingAdapter("bookName")
        fun TextView.setBookName(item: SearchInfoHolder?) {
            item?.let {
                text = item.bookTitle
            }
        }
    }
}