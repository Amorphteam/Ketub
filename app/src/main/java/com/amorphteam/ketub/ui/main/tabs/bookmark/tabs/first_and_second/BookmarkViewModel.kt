package com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.first_and_second

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.ui.main.tabs.bookmark.database.BookmarkDatabaseDao
import com.amorphteam.ketub.ui.main.tabs.bookmark.database.BookmarkRepository
import com.amorphteam.ketub.ui.main.tabs.bookmark.model.BookmarkModel
import com.amorphteam.ketub.utility.TempData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarkViewModel(private val bookmarkDatabaseDao: BookmarkDatabaseDao, private val catName: String) :
    ViewModel() {

    var startEpubAct = MutableLiveData<Boolean>()

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val repository: BookmarkRepository = BookmarkRepository(bookmarkDatabaseDao)

    private var _allBookmarks = MutableLiveData<List<BookmarkModel>>()
    val allBookmarks: LiveData<List<BookmarkModel>>
        get() = _allBookmarks

    init {
        initializeBookmarks()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun initializeBookmarks() {
        uiScope.launch {
            _allBookmarks.value = getAllBookmarks(catName)
        }
    }

    private suspend fun getAllBookmarks(catName:String): List<BookmarkModel>? {
        return withContext(Dispatchers.IO) {
            val bookmark = repository.getAllBookmarks(catName)
            bookmark
        }
    }

    fun deleteBookmark(id: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                repository.delete(id)
            }
            _allBookmarks.value = getAllBookmarks(catName)
        }
    }

    //TODO: INSERT BOOKMARK MUST COMPLETE

    fun insertBookmark() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                repository.insert(TempData.bookMarkArray[(0 until TempData.bookMarkArray.size).random()])
            }
            _allBookmarks.value = getAllBookmarks(catName)
        }
    }

    fun openEpubAct() {
        startEpubAct.value = true
    }

    companion object{
        @JvmStatic
        @BindingAdapter("bookName")
        fun TextView.setBookName(item: BookmarkModel?) {
            item?.let {
                text = item.bookName
            }
        }

        @JvmStatic
        @BindingAdapter("bookTitle")
        fun TextView.setBookTitle(item: BookmarkModel?) {
            item?.let {
                text = item.bookTitle
            }
        }
    }

}