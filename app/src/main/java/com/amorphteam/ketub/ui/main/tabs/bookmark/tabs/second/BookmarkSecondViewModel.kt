package com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.second

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.ui.main.tabs.bookmark.database.BookmarkDatabaseDao
import com.amorphteam.ketub.ui.main.tabs.bookmark.database.BookmarkRepository
import com.amorphteam.ketub.ui.main.tabs.bookmark.model.BookmarkModel
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.TempData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarkSecondViewModel(private val bookmarkDatabaseDao: BookmarkDatabaseDao) :
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
            _allBookmarks.value = getAllBookmarksFromDatabase()
            Log.i(Keys.LOG_NAME, "uiScope.launch.initializeBookmarks")
        }
    }

    private suspend fun getAllBookmarksFromDatabase(): List<BookmarkModel>? {
        Log.i(Keys.LOG_NAME, "getAllBookmarksFromDatabase")

        return withContext(Dispatchers.IO) {
            val bookmark = repository.getAllBookmarks()
            bookmark
        }
    }

    fun deleteBookmark(id: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                repository.delete(id)
            }
            _allBookmarks.value = getAllBookmarksFromDatabase()
        }
    }

    //TODO: INSERT BOOKMARK MUST COMPLETE

    fun insertBookmark() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                repository.insert(TempData.bookMarkArray[(0 until TempData.bookMarkArray.size).random()])
            }
            _allBookmarks.value = getAllBookmarksFromDatabase()
        }
    }

    fun openEpubAct() {
        startEpubAct.value = true
    }
}