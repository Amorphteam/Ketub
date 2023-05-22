package com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.first_and_second

import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.database.reference.ReferenceDatabaseDao
import com.amorphteam.ketub.database.reference.ReferenceRepository
import com.amorphteam.ketub.model.ReferenceModel
import com.amorphteam.ketub.utility.DatabaseReferenceHelper
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.TempData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarkViewModel(
    private val referenceRepository: ReferenceRepository,
    private val catName: String,
    private val singleBookName:String
) :
    ViewModel() {
    private var databaseReferenceHelper: DatabaseReferenceHelper? = DatabaseReferenceHelper.getInstance()
    private var _allBookmarks = MutableLiveData<List<ReferenceModel>>()
    val allBookmarks: LiveData<List<ReferenceModel>>
        get() = _allBookmarks

    init {
        if (bookMarkForAllBook()) {
            getAllBookMarksForAllBooks()
        }else {
            getAllBookmarksForSingleBook()
        }
    }

    private fun getAllBookmarksForSingleBook() {
        databaseReferenceHelper?.getOfflineReferenceSingleBook(
            referenceRepository,
            _allBookmarks,
            singleBookName
        )
    }

    private fun getAllBookMarksForAllBooks() {
        databaseReferenceHelper?.getOfflineReference(
            referenceRepository,
            _allBookmarks,
            catName.split(" ").first()
        )
    }

    private fun bookMarkForAllBook():Boolean{
        var forAllBooks = false

        if (catName.isEmpty()) forAllBooks = false
        if (singleBookName.isEmpty()) forAllBooks = true
        return forAllBooks
    }

    override fun onCleared() {
        super.onCleared()
        databaseReferenceHelper = null
    }

    fun deleteBookmark(it: Int) {
        databaseReferenceHelper?.deleteBookmark(it, referenceRepository)
        if(bookMarkForAllBook()){
            getAllBookMarksForAllBooks()
        }else{
            getAllBookmarksForSingleBook()
        }

    }


    companion object {
        @JvmStatic
        @BindingAdapter("bookName")
        fun TextView.setBookName(item: ReferenceModel?) {
            item?.let {
                text = item.bookName
            }
        }

        @JvmStatic
        @BindingAdapter("bookTitle")
        fun TextView.setBookTitle(item: ReferenceModel?) {
            item?.let {
                text = item.title
            }
        }
    }

}