package com.amorphteam.ketub.ui.epub

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.utility.Keys
import com.mehdok.fineepublib.epubviewer.epub.ManifestItem
import com.mehdok.fineepublib.epubviewer.epub.NavPoint
import com.mehdok.fineepublib.epubviewer.jsepub.JSBook
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException

class EpubViewModel() : ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _spineArray = MutableLiveData<ArrayList<ManifestItem>>()
    val spineArray: LiveData<ArrayList<ManifestItem>>
        get() = _spineArray



    fun getBookAddress(bookAddress: String?) {
        uiScope.launch {
            parseBook(bookAddress)
        }
    }

    private suspend fun parseBook(bookAddress: String?) {
        getBook(bookAddress).collect { book: JSBook? ->
            if (book != null) {
                _spineArray.value = book.spine
            } else {
                Log.i(Keys.LOG_NAME, "Book is null")
            }
        }
    }

    private suspend fun getBook(bookAddress: String?): Flow<JSBook?> = flow {
        var book: JSBook? = null
        try {
            withContext(Dispatchers.IO) {
                book = JSBook(bookAddress)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        emit(book)
    }
}