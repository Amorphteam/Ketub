package com.amorphteam.ketub.ui.main.tabs.toc.tabs.first_and_second

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.model.TocFirstChildItem
import com.amorphteam.ketub.model.TocGroupItem
import com.amorphteam.ketub.utility.TempData
import kotlinx.coroutines.*

class TocViewModel(val catName: String) : ViewModel() {
    var startEpubAct = MutableLiveData<Boolean>()
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _tocGroupItems = MutableLiveData<List<TocGroupItem>>()
    val tocGroupItems: LiveData<List<TocGroupItem>>
        get() = _tocGroupItems

    init {
        initializeToc()

    }

    private fun initializeToc() {
        uiScope.launch {
            _tocGroupItems.value = getToc(catName)
        }

    }

    private suspend fun getToc(catName: String): List<TocGroupItem>? {
        return withContext(Dispatchers.IO) {
            //TODO: // SHOULD GET IT FROM EPUBS USING CATNAME
            val toc = TempData.indexItems
            toc
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun openEpubAct() {
        startEpubAct.value = true
    }

    companion object {
        @JvmStatic
        @BindingAdapter("bookNameGroup")
        fun TextView.setBookNameGroup(item: TocGroupItem?) {
            item?.let {
                text = item.bookName
            }
        }

        @JvmStatic
        @BindingAdapter("bookTitleGroup")
        fun TextView.setBookTitleGroup(item: TocGroupItem?) {
            item?.let {
                text = item.bookTitle
            }
        }

        @JvmStatic
        @BindingAdapter("bookNameChild")
        fun TextView.setBookNameChild(item: TocFirstChildItem?) {
            item?.let {
                text = item.bookName
            }
        }

        @JvmStatic
        @BindingAdapter("bookTitleChild")
        fun TextView.setBookTitleChild(item: TocFirstChildItem?) {
            item?.let {
                text = item.bookTitle
            }
        }
    }
}