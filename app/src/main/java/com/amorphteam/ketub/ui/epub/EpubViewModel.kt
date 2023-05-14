package com.amorphteam.ketub.ui.epub

import android.util.Log
import android.widget.SeekBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.model.BookHolder
import com.amorphteam.ketub.model.FontName
import com.amorphteam.ketub.model.FontSize
import com.amorphteam.ketub.model.LineSpace
import com.amorphteam.ketub.ui.adapter.EpubVerticalAdapter
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.PreferencesManager
import com.amorphteam.ketub.utility.StyleBookPreferences
import com.mehdok.fineepublib.epubviewer.epub.ManifestItem
import com.mehdok.fineepublib.epubviewer.jsepub.JSBook
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import kotlin.collections.ArrayList

class EpubViewModel() : ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _fullScreen = MutableLiveData<Boolean>()
    val fullScreen: LiveData<Boolean>
        get() = _fullScreen

    private val _adapter = MutableLiveData<EpubVerticalAdapter>()
    val adapter: LiveData<EpubVerticalAdapter>
        get() = _adapter

    private val _spineArray = MutableLiveData<ArrayList<ManifestItem>>()
    val spineArray: LiveData<ArrayList<ManifestItem>>
        get() = _spineArray


    val currentFontSize = MutableLiveData<Int>()
    val currentLineSpace = MutableLiveData<Int>()



    init {
        _fullScreen.value = true
    }

     fun handleSavedStyle(preferencesManager: PreferencesManager) {
         val styleBookPref:StyleBookPreferences = preferencesManager.getStyleBookPref()
         currentLineSpace.value = styleBookPref.lineSpace.ordinal
         currentFontSize.value = styleBookPref.fontSize.ordinal
    }

    fun getBookAddress(bookAddress: String?) {
        uiScope.launch {
            parseBook(bookAddress)
        }
    }

    private suspend fun parseBook(bookAddress: String?) {
        getBook(bookAddress).collect { book: JSBook? ->
            if (book != null) {
                BookHolder.instance?.jsBook = book
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


    fun toggle(){
        _fullScreen.value = _fullScreen.value != true
    }

    fun setAdapter(adapter: EpubVerticalAdapter){
        _adapter.value = adapter
    }

    fun updateFontSizeSeekBar(seekBar: SeekBar, progress: Int, fromUser: Boolean){
        currentFontSize.value = progress
    }

    fun updateLineSpaceSeekBar(seekBar: SeekBar, progress: Int, fromUser: Boolean){
        currentLineSpace.value = progress
    }



}

