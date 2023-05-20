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
import com.mehdok.fineepublib.epubviewer.epub.Book
import kotlinx.coroutines.*
import okio.IOException

class TocViewModel(val catName: String, val bookRepository: BookRepository) : ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _treeTocNavResult = MutableLiveData<NavResult?>()
    val treeTocNavResult: LiveData<NavResult?>
        get() = _treeTocNavResult

init {

    Log.i(Keys.LOG_NAME, "wwwwgetIndexList(context).navTrees.size.toString()")

}
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getIndex(context: Context){
        uiScope.launch {
        _treeTocNavResult.value = getIndexList(context)
        }
    }

   private suspend fun getIndexList(context: Context):NavResult{
            val catBooks = withContext(Dispatchers.IO) {
                bookRepository.getAllBooks()
            }

            val result = ArrayList<IndexesInfo>()
            val treeResult = ArrayList<TreeBookHolder>()
            val fileManager = FileManager(context)

            for (catBook in catBooks) {
                Log.i(Keys.LOG_NAME, "catbooks is ${catBooks.size}")
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

                    Log.i(Keys.LOG_NAME, "tree is: ${tree?.size}")
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

            return withContext(Dispatchers.Main) {
                val navResult = NavResult(result, treeResult)
                navResult
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