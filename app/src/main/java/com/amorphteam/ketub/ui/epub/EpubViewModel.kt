package com.amorphteam.ketub.ui.epub

import android.graphics.fonts.Font
import android.util.Log
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.R
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
    val currentFontName = MutableLiveData<Int>()
    val lastPageSeen = MutableLiveData<Int>()
    lateinit var styleBookPref: StyleBookPreferences
    lateinit var preferencesManager: PreferencesManager
    var styleListener: ArrayList<StyleListener>? = ArrayList()

    init {
        _fullScreen.value = true
    }

    fun setPrefManage(preferencesManager: PreferencesManager){
        this.preferencesManager = preferencesManager
    }
    fun handleLastPageSeen(bookAddress: String?){
        lastPageSeen.value = bookAddress?.let { preferencesManager.getLastPageSeen(it) }
    }
    fun handleSavedStyle() {
        styleBookPref = preferencesManager.getStyleBookPref()
        currentLineSpace.value = styleBookPref.lineSpace.ordinal
        currentFontSize.value = styleBookPref.fontSize.ordinal
        currentQuickStyle.value = styleBookPref.quickStyle.ordinal
        currentFontName.value = styleBookPref.fontName.ordinal
        currentTheme.value = styleBookPref.theme.ordinal
        handleQuickStyle(QuickStyle.from(currentQuickStyle.value!!))
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

    fun setFullScreenWindow(){
        _fullScreen.value = true
    }
    fun onDismissSheet() {
        _dismissSheet.value = true

    }



    fun updateFontSizeSeekBar(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        val fontSize = FontSize.from(progress)
        styleBookPref.fontSize = fontSize
        currentFontSize.value = progress
        for (listener in styleListener!!) {
            listener.changeFontSize(progress)
        }
    }

    fun updateLineSpaceSeekBar(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        val lineSpace = LineSpace.from(progress)
        styleBookPref.lineSpace = lineSpace
        currentLineSpace.value = progress
        for (listener in styleListener!!) {
            listener.changeLineSpace(progress)
        }
    }



    override fun onCleared() {
        super.onCleared()
        preferencesManager.saveStyleBookPref(styleBookPref)


    }
    fun updateQuickStyle(id: Int){
        currentQuickStyle.value = id
        val quickStyle = QuickStyle.from(id)
        handleQuickStyle(quickStyle)
        styleBookPref.quickStyle = quickStyle
    }

    fun handleQuickStyle(quickStyle: QuickStyle){
        when (quickStyle){
            QuickStyle.DEFAULT -> {
                currentFontSize.value = FontSize.SIZE1.number
                currentLineSpace.value = LineSpace.SPACE1.number
                currentTheme.value = Theme.BASE.number
            }

            QuickStyle.READABILITY -> {
                currentFontSize.value = FontSize.SIZE4.number
                currentLineSpace.value = LineSpace.SPACE4.number
                currentTheme.value = Theme.BASE.number
            }
            QuickStyle.DARKMODE -> {
                currentTheme.value = Theme.DARK.number

            }

            QuickStyle.HIGHCONTRAST -> {
                //TODO: //ADD HIGH CONTRAST STYLE
            }

        }
        handleListenerForAllTheme()
        saveAllThemeInBookPref()
    }

    private fun saveAllThemeInBookPref() {
        styleBookPref.quickStyle = QuickStyle.from(currentQuickStyle.value!!)
        styleBookPref.theme = Theme.from(currentTheme.value!!)
        styleBookPref.fontSize = FontSize.from(currentFontSize.value!!)
        styleBookPref.lineSpace = LineSpace.from(currentLineSpace.value!!)
        styleBookPref.fontName = FontName.from(currentFontName.value!!)
    }

    private fun handleListenerForAllTheme() {
        for (listener in styleListener!!) {
            listener.changeTheme(currentTheme.value)
            listener.changeFontSize(currentFontSize.value)
            listener.changeLineSpace(currentLineSpace.value)
            listener.changeFontName(currentFontName.value)
        }
    }

    fun updateTheme(id: Int){
        currentTheme.value = id
        val theme = Theme.from(id)
        styleBookPref.theme = theme
        for (listener in styleListener!!){
            listener.changeTheme(id)
        }
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
                        currentFontName.value = selectedItemId
                        styleBookPref.fontName = FontName.from(selectedItemId)
                        for (listener in styleListener!!){
                            listener.changeFontName(selectedItemId)
                        }
                    }
                }

                chipGroup.addView(chip)
            }
        }
    }
    companion object {
        @JvmStatic
        @BindingAdapter("bind:tintConditionally")
        fun setImageButtonTintConditionally(imageButton: ImageButton, colored: Boolean) {
            var color = R.color.secondary2

            if (colored) {
                color = R.color.secondary1
            }

            val context = imageButton.context
            val drawable = ContextCompat.getColor(context, color)
            imageButton.setColorFilter(drawable)
        }
    }
}






