package com.amorphteam.ketub.ui.search

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.amorphteam.ketub.ui.search.model.SearchModel

@BindingAdapter("searchResult")
fun TextView.setSearchResult(item: SearchModel?) {
    item?.let {
        text = item.searchResult
    }
}

@BindingAdapter("bookName")
fun TextView.setBookName(item: SearchModel?) {
    item?.let {
        text = item.bookName
    }
}