package com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.first_and_second

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.database.reference.ReferenceDatabaseDao
import com.amorphteam.ketub.database.reference.ReferenceRepository
import com.amorphteam.ketub.model.ReferenceModel
import com.amorphteam.ketub.utility.DatabaseReferenceHelper
import com.amorphteam.ketub.utility.TempData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarkViewModel(
    private val referenceRepository: ReferenceRepository,
    private val catName: String
) :
    ViewModel() {
    private var databaseReferenceHelper: DatabaseReferenceHelper? = DatabaseReferenceHelper.getInstance()
    private var _allBookmarks = MutableLiveData<List<ReferenceModel>>()
    val allBookmarks: LiveData<List<ReferenceModel>>
        get() = _allBookmarks

    init {
        databaseReferenceHelper?.getOfflineReference(referenceRepository, _allBookmarks, catName)
    }

    override fun onCleared() {
        super.onCleared()
        databaseReferenceHelper = null
    }

    fun deleteBookmark(it: Int) {
        databaseReferenceHelper?.deleteBookmark(it, referenceRepository)
        databaseReferenceHelper?.getOfflineReference(referenceRepository, _allBookmarks, catName)
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