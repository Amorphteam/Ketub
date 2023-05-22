package com.amorphteam.ketub.ui.search

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.database.book.BookRepository
import com.amorphteam.ketub.model.BookModel
import com.amorphteam.ketub.model.SearchModel
import com.amorphteam.ketub.utility.DatabaseBookHelper
import com.amorphteam.ketub.utility.EpubHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel(val bookRepository: BookRepository, val bookPath:String) : ViewModel() {
    private var databaseBookHelper: DatabaseBookHelper? = DatabaseBookHelper.getInstance()
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _allBooks = MutableLiveData<List<BookModel>>()
    val allBooks: LiveData<List<BookModel>>
        get() = _allBooks


    private val _results = MutableLiveData<ArrayList<SearchModel>>()
    val results: LiveData<ArrayList<SearchModel>>
        get() = _results
    init {
        if (bookPath.isEmpty()) {
            getAllBooks()
        }else {
            getBook()
        }
    }

    private fun getBook() {
        databaseBookHelper?.getBook(bookPath, bookRepository, _allBooks)
    }


    private fun getAllBooks() {
        databaseBookHelper?.getAllBooks(bookRepository, _allBooks)
    }


    override fun onCleared() {
        super.onCleared()
        databaseBookHelper = null
        viewModelJob.cancel()
    }

    fun searchAllBooks(searchHelper: SearchHelper, it: List<String>, s: String) {
        uiScope.launch{
            _results.value?.clear()
            var currentResults = ArrayList<SearchModel>()
            currentResults.clear()
            searchHelper.searchAllBooks(it, s).collect{
                currentResults = _results.value ?: ArrayList()
                currentResults.addAll(it)
                _results.value = currentResults
            }
        }
    }

    fun clearList(){
        _results.value?.clear()
        _results.value = ArrayList()
    }
    companion object {
        @JvmStatic
        @BindingAdapter("searchResult")
        fun TextView.setSearchResult(item: SearchModel?) {
            item?.let {
                text = item.spanna
            }
        }

        @JvmStatic
        @BindingAdapter("bookName")
        fun TextView.setBookName(item: SearchModel?) {
            item?.let {
                text = item.bookTitle
            }
        }
    }
}