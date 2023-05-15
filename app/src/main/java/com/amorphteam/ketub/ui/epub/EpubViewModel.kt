package com.amorphteam.ketub.ui.epub

import android.util.Log
import android.widget.SeekBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.model.*
import com.amorphteam.ketub.ui.adapter.EpubVerticalAdapter
import com.amorphteam.ketub.ui.epub.fragments.StyleListener
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.PreferencesManager
import com.amorphteam.ketub.utility.StyleBookPreferences
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
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


    private val _dismissSheet = MutableLiveData<Boolean>()
    val dismissSheet: LiveData<Boolean>
        get() = _dismissSheet

    val currentFontSize = MutableLiveData<Int>()
    val currentLineSpace = MutableLiveData<Int>()
    val currentQuickStyle = MutableLiveData<Int>()
    val currentTheme = MutableLiveData<Int>()

    lateinit var styleBookPref: StyleBookPreferences
    lateinit var preferencesManager: PreferencesManager
    var styleListener: StyleListener? = null

    init {
        _fullScreen.value = true
    }

    fun handleSavedStyle(preferencesManager: PreferencesManager) {
        this.preferencesManager = preferencesManager
        styleBookPref = preferencesManager.getStyleBookPref()
        currentLineSpace.value = styleBookPref.lineSpace.ordinal
        currentFontSize.value = styleBookPref.fontSize.ordinal
        currentQuickStyle.value = styleBookPref.quickStyle.ordinal
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


    fun toggle() {
        _fullScreen.value = _fullScreen.value != true
    }

    fun setAdapter(adapter: EpubVerticalAdapter) {
        _adapter.value = adapter
    }

    fun updateFontSizeSeekBar(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        val fontSize = FontSize.from(progress)
        styleBookPref.fontSize = fontSize
        currentFontSize.value = progress
        styleListener?.changeFontSize(progress)
    }

    fun updateLineSpaceSeekBar(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        val lineSpace = LineSpace.from(progress)
        styleBookPref.lineSpace = lineSpace
        currentLineSpace.value = progress
        styleListener?.changeLineSpace(progress)
    }

    fun onDismissSheet() {
        _dismissSheet.value = _dismissSheet.value != true
    }


    override fun onCleared() {
        super.onCleared()
        preferencesManager.saveStyleBookPref(styleBookPref)
    }
    fun updateQuickStyle(id: Int){
        currentQuickStyle.value = id
        val quickStyle = QuickStyle.from(id)
        styleBookPref.quickStyle = quickStyle
    }

    fun updateTheme(id: Int){
        currentTheme.value = id
        val theme = Theme.from(id)
        styleBookPref.theme = theme
    }
    fun setChips(chipGroup: ChipGroup, items: List<FontName>?) {
        chipGroup.removeAllViews()
        val selectedChip = styleBookPref.fontName.ordinal
        items?.let {
            for (item in items) {
                val chip = Chip(chipGroup.context)
                chip.text = item.name
                chip.tag = item.number

                chip.isCheckable = true
                if (item.number == selectedChip) {
                    chip.isChecked = true
                }
                chip.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        // Handle chip selection
                        val selectedItemId = chip.tag as Int
                        styleBookPref.fontName = FontName.from(selectedItemId)
                        styleListener?.changeFontName(selectedItemId)
                    }
                }

                chipGroup.addView(chip)
            }
        }
    }


}

