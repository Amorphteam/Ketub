package com.amorphteam.ketub.utility

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.amorphteam.ketub.database.book.BookRepository
import com.amorphteam.ketub.model.*
import com.mehdok.fineepublib.epubviewer.epub.Book
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException

class TocHelper {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    fun handleIndexList(context: Context, bookRepository: BookRepository, categories: List<CategoryModel>, navResult: MutableLiveData<NavResult?>) {
        uiScope.launch {
            getIndexList(context, bookRepository, categories)
                .flowOn(Dispatchers.IO)
                .collect { result ->
                    navResult.value = result
                }
        }
    }

    private fun getIndexList(context: Context, bookRepository: BookRepository, categories: List<CategoryModel>): Flow<NavResult> = flow {
        var books: List<BookModel>? = null
        val allIds = ArrayList<Int>()
        for (item in categories) {
            allIds.add(item.id)
        }

        books = withContext(Dispatchers.IO) {
            bookRepository.getAllBooks(allIds)
        }


        val result = ArrayList<IndexesInfo>()
        val treeResult = ArrayList<TreeBookHolder>()
        val fileManager = FileManager(context)

        for (item in books) {
            try {
                val book = Book(fileManager.getBookAddress(item.bookPath!!))
                for (navPoint in book.tableOfContents.navPoints) {
                    result.add(
                        IndexesInfo(
                            book.bookName,
                            navPoint,
                            item.bookPath
                        )
                    )
                }

                val tree = NavTreeCreator.getNavTree(
                    book.tableOfContents.navPoints,
                    item.bookPath
                )

                // if it's the first time
                if (treeResult.isEmpty()) {
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
                            if (it1.data.navPoint.navLabel == it.getNode().data.navPoint.navLabel) {
                                // root exists
                                it.getNode().children.addAll(it1.children)
                                newTreeIter.remove()
                            }
                        }
                    }
                }

                if (tree!!.isNotEmpty()) {
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

        emit(NavResult(result, treeResult))
    }.flowOn(Dispatchers.IO)


}