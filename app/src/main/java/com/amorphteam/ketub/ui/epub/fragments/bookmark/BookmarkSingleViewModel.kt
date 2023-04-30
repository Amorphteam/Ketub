package com.amorphteam.ketub.ui.epub.fragments.bookmark

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.ui.main.tabs.bookmark.database.BookmarkDatabaseDao
import com.amorphteam.ketub.ui.main.tabs.bookmark.database.BookmarkRepository
import com.amorphteam.ketub.ui.main.tabs.bookmark.model.BookmarkModel
import com.amorphteam.ketub.utility.Keys
import kotlinx.coroutines.*

class BookmarkSingleViewModel(private val bookmarkDatabaseDao: BookmarkDatabaseDao) : ViewModel() {
    var startEpubFrag = MutableLiveData<Boolean>()

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val repository: BookmarkRepository = BookmarkRepository(bookmarkDatabaseDao)

    private var _bookName = MutableLiveData<String>()
    val bookName: LiveData<String>
        get() = _bookName

    private var _allBookmarks = MutableLiveData<List<BookmarkModel>>()
    val allBookmarks: LiveData<List<BookmarkModel>>
        get() = _allBookmarks

    init {
        _bookName.value = "نصوص معاصرة"
        initializeBookmarks()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun initializeBookmarks() {
        uiScope.launch {
            _allBookmarks.value = getAllBookmarksForSingleBookFromDatabase(_bookName.value!!)
            Log.i(Keys.LOG_NAME, "uiScope.launch.initializeBookmarks")
        }
    }

    private suspend fun getAllBookmarksForSingleBookFromDatabase(bookName:String): List<BookmarkModel> {
        return withContext(Dispatchers.IO) {
            val bookmark = repository.getAllBookmarksForSingleBook(bookName)
            bookmark
        }
    }

    fun deleteBookmark(id: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                repository.delete(id)
            }
            _allBookmarks.value = getAllBookmarksForSingleBookFromDatabase(_bookName.value!!)
        }
    }

    fun openEpubFrag() {
        startEpubFrag.value = true
    }
}