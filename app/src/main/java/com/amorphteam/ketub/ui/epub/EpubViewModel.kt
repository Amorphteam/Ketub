package com.amorphteam.ketub.ui.epub

import android.graphics.Typeface
import android.util.Log
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.R
import com.amorphteam.ketub.database.reference.ReferenceRepository
import com.amorphteam.ketub.model.*
import com.amorphteam.ketub.utility.DatabaseReferenceHelper
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.PreferencesManager
import com.amorphteam.ketub.utility.StyleBookPreferences
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mehdok.fineepublib.epubviewer.epub.Book
import com.mehdok.fineepublib.epubviewer.epub.ManifestItem
import com.mehdok.fineepublib.epubviewer.jsepub.JSBook
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import kotlin.collections.ArrayList

class EpubViewModel(val referenceRepository: ReferenceRepository) : ViewModel() {
    val pageNumber = MutableLiveData<String>()
    val bookName = MutableLiveData<String>()
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var databaseReferenceHelper: DatabaseReferenceHelper? =
        DatabaseReferenceHelper.getInstance()

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

    private val _bookmarkSelected = MutableLiveData<Boolean>()
    val bookmarkSelected: LiveData<Boolean>
        get() = _bookmarkSelected


    private val _lastPageSeen = MutableLiveData<Int?>()
    val lastPageSeen: LiveData<Int?>
        get() = _lastPageSeen

    lateinit var styleBookPref: StyleBookPreferences
    lateinit var preferencesManager: PreferencesManager
    var styleListener: ArrayList<StyleListener>? = ArrayList()

    init {
        _fullScreen.value = true
    }

    fun setPrefManage(preferencesManager: PreferencesManager) {
        this.preferencesManager = preferencesManager
    }

    fun handleLastPageSeen(bookAddress: String?) {
        _lastPageSeen.value = bookAddress?.let {
            preferencesManager.getLastPageSeen(it)
        }
    }

    fun handleBookmarkPage(page: Int) {
        _lastPageSeen.value = page
    }

    fun handleSavedStyle() {
        styleBookPref = preferencesManager.getStyleBookPref()
        currentLineSpace.value = styleBookPref.lineSpace.ordinal
        currentFontSize.value = styleBookPref.fontSize.ordinal
        currentQuickStyle.value = styleBookPref.quickStyle.ordinal
        currentFontName.value = styleBookPref.fontName.ordinal
        currentTheme.value = styleBookPref.theme.ordinal

        Log.i("AJc", currentFontSize.value.toString())

        handleQuickStyle(QuickStyle.from(currentQuickStyle.value!!))

        Log.i("AJc", currentFontSize.value.toString())
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

    fun setFullScreenWindow() {
        _fullScreen.value = true
    }

    fun onDismissSheet() {
        _dismissSheet.value = true

    }


    fun updateFontSizeSeekBar(seekBar: SeekBar?, progress: Int, fromUser: Boolean?) {
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
        saveStyles()
        databaseReferenceHelper = null

    }

    fun saveStyles() {
        preferencesManager.saveStyleBookPref(styleBookPref)
    }

    fun updateQuickStyle(id: Int) {
        currentQuickStyle.value = id
        val quickStyle = QuickStyle.from(id)
        handleQuickStyle(quickStyle)
        styleBookPref.quickStyle = quickStyle
    }

    private fun handleQuickStyle(quickStyle: QuickStyle) {
        when (quickStyle) {
            QuickStyle.DEFAULT -> {
                currentFontSize.value = FontSize.SIZE2.number
                currentLineSpace.value = LineSpace.SPACE2.number
                currentTheme.value = Theme.BASE.number
            }

            QuickStyle.READABILITY -> {
                currentFontSize.value = FontSize.SIZE4.number
                currentLineSpace.value = LineSpace.SPACE4.number
            }

            QuickStyle.DARKMODE -> {
                currentTheme.value = Theme.DARK.number

            }

            QuickStyle.HIGHCONTRAST -> {
                currentTheme.value = Theme.CONTRAST.number
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

    fun updateTheme(id: Int) {
        currentTheme.value = id
        val theme = Theme.from(id)
        styleBookPref.theme = theme
        for (listener in styleListener!!) {
            listener.changeTheme(id)
        }
    }

    fun setChips(chipGroup: ChipGroup, items: List<FontName>?) {
        chipGroup.removeAllViews()
        val selectedChip = styleBookPref.fontName.ordinal
        items?.let {
            for (item in items) {
                val chip = Chip(chipGroup.context)
                chip.text = item.name.replace("FONT", "الخط ")
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
                        for (listener in styleListener!!) {
                            listener.changeFontName(selectedItemId)
                        }
                    }
                }

                chipGroup.addView(chip)
            }
        }
    }

    fun handleNavUriPage(navUri: String?) {
        val pageIndex =
            BookHolder.instance?.jsBook?.getResourceNumber(Book.resourceName2Url(navUri))
        _lastPageSeen.value = pageIndex
    }

    fun bookmarkCurrentPage(bookPath: String, bookName: String, navIndex: Int, title: String) {

        val referenceItem = ReferenceModel(0, title, bookName, bookPath, navIndex, null, null)

        databaseReferenceHelper?.insertBookmark(referenceRepository, referenceItem)

    }

    fun checkBookmark(bookName: String, navIndex: Int) {
        databaseReferenceHelper?.getSelected(
            bookName,
            navIndex,
            referenceRepository,
            _bookmarkSelected
        )
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






