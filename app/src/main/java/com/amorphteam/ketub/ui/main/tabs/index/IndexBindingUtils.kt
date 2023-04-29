package com.amorphteam.ketub.ui.main.tabs.index

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.amorphteam.ketub.ui.main.tabs.bookmark.model.BookmarkModel
import com.amorphteam.ketub.ui.main.tabs.index.model.IndexFirstChildItem
import com.amorphteam.ketub.ui.main.tabs.index.model.IndexGroupItem

@BindingAdapter("bookNameGroup")
fun TextView.setBookNameGroup(item: IndexGroupItem?) {
    item?.let {
        text = item.bookName
    }
}

@BindingAdapter("bookTitleGroup")
fun TextView.setBookTitleGroup(item: IndexGroupItem?) {
    item?.let {
        text = item.bookTitle
    }
}

@BindingAdapter("bookNameChild")
fun TextView.setBookNameChild(item: IndexFirstChildItem?) {
    item?.let {
        text = item.bookName
    }
}

@BindingAdapter("bookTitleChild")
fun TextView.setBookTitleChild(item: IndexFirstChildItem?) {
    item?.let {
        text = item.bookTitle
    }
}