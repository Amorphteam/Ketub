package com.amorphteam.ketub.ui.main.tabs.toc

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.amorphteam.ketub.ui.main.tabs.toc.model.TocFirstChildItem
import com.amorphteam.ketub.ui.main.tabs.toc.model.TocGroupItem

@BindingAdapter("bookNameGroup")
fun TextView.setBookNameGroup(item: TocGroupItem?) {
    item?.let {
        text = item.bookName
    }
}

@BindingAdapter("bookTitleGroup")
fun TextView.setBookTitleGroup(item: TocGroupItem?) {
    item?.let {
        text = item.bookTitle
    }
}

@BindingAdapter("bookNameChild")
fun TextView.setBookNameChild(item: TocFirstChildItem?) {
    item?.let {
        text = item.bookName
    }
}

@BindingAdapter("bookTitleChild")
fun TextView.setBookTitleChild(item: TocFirstChildItem?) {
    item?.let {
        text = item.bookTitle
    }
}