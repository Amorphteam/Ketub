package com.amorphteam.ketub.ui.main.tabs.toc.tabs.first_and_second

import android.content.Context
import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.database.book.BookRepository
import com.amorphteam.ketub.model.*
import com.amorphteam.ketub.utility.DatabaseBookHelper
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.TocHelper
import kotlinx.coroutines.*

class TocViewModel(val catName: String, val bookRepository: BookRepository) : ViewModel() {
    private var databaseBookHelper: DatabaseBookHelper? = DatabaseBookHelper.getInstance()
    private var tocHelper: TocHelper? = TocHelper()
    private var _catBooksNewItems = MutableLiveData<List<CategoryModel>>()
    val catBooksNewItems: LiveData<List<CategoryModel>>
        get() = _catBooksNewItems

    private val _treeTocNavResult = MutableLiveData<NavResult?>()
    val treeTocNavResult: LiveData<NavResult?>
        get() = _treeTocNavResult

init {
}
    override fun onCleared() {
        super.onCleared()
        tocHelper = null
        databaseBookHelper = null
    }

    fun getCat(){
            databaseBookHelper?.getCats(catName, bookRepository, _catBooksNewItems)
    }

    fun getIndex(context: Context){
        tocHelper?.handleIndexList(context, bookRepository,
            _catBooksNewItems.value!!, _treeTocNavResult)
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