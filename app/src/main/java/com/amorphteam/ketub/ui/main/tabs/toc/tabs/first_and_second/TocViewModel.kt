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
import com.amorphteam.ketub.utility.FileManager
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.NavTreeCreator.getNavTree
import com.amorphteam.ketub.utility.TempData
import com.mehdok.fineepublib.epubviewer.epub.Book
import kotlinx.coroutines.*
import okio.IOException

class TocViewModel(val catName: String, val bookRepository: BookRepository) : ViewModel() {
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

    fun getIndexList(context: Context){
        uiScope.launch {
            val catBooks = withContext(Dispatchers.IO) {
                bookRepository.getAllBooks()
            }

            val result = ArrayList<IndexesInfo>()
            val treeResult = ArrayList<TreeBookHolder>()
            val fileManager = FileManager(context)

            for (catBook in catBooks) {
                try {
                    val book = Book(fileManager.getBookAddress(catBook.bookPath!!))
                    for (navPoint in book.tableOfContents.navPoints) {
                        result.add(
                            IndexesInfo(
                                book.bookName,
                                navPoint,
                                catBook.bookPath
                            )
                        )
                    }

                    val tree = getNavTree(
                        book.tableOfContents.navPoints,
                        catBook.bookPath
                    )
                    // if it's the first time
                    if (treeResult.size == 0) {
                        val treeAndPath = ArrayList<TreeAndPath>(tree!!.size)
                        for (t in tree) {
                            treeAndPath.add(TreeAndPath(t))
                        }
                        treeResult.add(TreeBookHolder(book.bookName, treeAndPath))
                        continue
                    }

                    for (existingTree in treeResult) {
                        for (it in existingTree.getNavTree()) {
                            val newTreeIter = tree?.iterator()
                            while (newTreeIter!!.hasNext()) {
                                val it1 = newTreeIter.next()
                                if (it1.data.navPoint.navLabel
                                    == it.getNode().data.navPoint.navLabel
                                ) {
                                    // root exists
                                    it.getNode().children.addAll(it1.children)
                                    newTreeIter.remove()
                                }
                            }
                        }
                    }

                    if (tree!!.size > 0) {
                        val treeAndPath = ArrayList<TreeAndPath>(tree.size)
                        for (t in tree) {
                            treeAndPath.add(TreeAndPath(t))
                        }
                        treeResult.add(TreeBookHolder(book.bookName, treeAndPath))
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            withContext(Dispatchers.Main) {
                val navResult = NavResult(result, treeResult)
                Log.i(Keys.LOG_NAME, navResult.toString())
            }
        }
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