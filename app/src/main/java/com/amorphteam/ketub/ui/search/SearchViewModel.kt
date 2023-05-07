package com.amorphteam.ketub.ui.search

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.model.SearchModel
import com.amorphteam.ketub.utility.TempData

class SearchViewModel : ViewModel() {

    private val _startEpubAct = MutableLiveData<Boolean>()
    val startEpubAct: LiveData<Boolean>
        get() = _startEpubAct


    fun getSearchList(): MutableLiveData<ArrayList<SearchModel>> {
        val array = MutableLiveData<ArrayList<SearchModel>>()
        array.value = TempData.searchResult
        return array
    }


    fun openEpubAct() {
        _startEpubAct.value = true
    }

    companion object {
        @JvmStatic
        @BindingAdapter("searchResult")
        fun TextView.setSearchResult(item: SearchModel?) {
            item?.let {
                text = item.searchResult
            }
        }

        @JvmStatic
        @BindingAdapter("bookName")
        fun TextView.setBookName(item: SearchModel?) {
            item?.let {
                text = item.bookName
            }
        }
    }
}