package com.amorphteam.ketub.ui.main.tabs.bookmark

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.amorphteam.ketub.ui.main.tabs.bookmark.model.BookmarkModel


@BindingAdapter("bookName")
fun TextView.setBookName(item: BookmarkModel?) {
    item?.let {
        text = item.bookName
    }
}

@BindingAdapter("bookTitle")
fun TextView.setBookTitle(item: BookmarkModel?) {
    item?.let {
        text = item.bookTitle
    }
}