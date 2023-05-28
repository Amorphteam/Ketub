package com.amorphteam.ketub.ui.search

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.BackgroundColorSpan
import android.util.Log
import com.amorphteam.ketub.R
import com.amorphteam.ketub.model.SearchHighlight
import com.amorphteam.ketub.model.SearchIndex
import com.amorphteam.ketub.model.SearchModel
import com.amorphteam.ketub.ui.search.searchmode.NormalSearcher
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.Keys.Companion.SEARCH_SURROUND_CHAR_NUM
import com.mehdok.fineepublib.epubviewer.epub.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okio.IOException
import org.apache.commons.lang3.StringEscapeUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import org.jsoup.select.Elements
import java.util.*

class SearchHelper(val context: Context) {
    private var searcher: BaseSearcher? = null
    private var flag = false
    private var mSearchCount = 0


    suspend fun searchAllBooks(allBooks: List<String>, word: String): Flow<ArrayList<SearchModel>> = flow {
        searcher = NormalSearcher()
        for (book in allBooks) {
            if (flag) break
            emit(searchSingleBook(book, word))
        }
    }


    private suspend fun searchSingleBook(address: String, sw: String): ArrayList<SearchModel> = withContext(Dispatchers.IO) {
        val tempResult = ArrayList<SearchModel>()
        var book: Book? = null
        var bookTitle = ""
        try {
            book = Book(address)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (book == null) return@withContext arrayListOf()

        bookTitle = book.metadata.title

        for (page in book.spine) {
            if (flag) {break}
            try {
                var searchCount = 0
                val pageResource = book.fetch(Book.resourceName2Url(page.href))
                val pageString = book.getPageString(pageResource.data)

                if (TextUtils.isEmpty(pageString)) { continue }
                var searchIndex = searchInString(pageString, sw, 0)

                while (searchIndex.startIndex >= 0) {
                    searchCount++
                    tempResult.add(
                        SearchModel(
                            address,
                            bookTitle,
                            page.href,
                            getHighlightedSection(searchIndex, pageString),
                            searchCount
                        )
                    )
                    searchIndex = searchInString(pageString, sw, searchIndex.lastIndex + 1)
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return@withContext tempResult
    }

    private fun getHighlightedSection(index: SearchIndex, wholeString: String): SpannableString {
        val sw = wholeString.substring(index.startIndex, index.lastIndex)
        val swLength = index.lastIndex - index.startIndex
        val lastIndex = wholeString.length
        val firstCutIndex =
            if (index.startIndex - SEARCH_SURROUND_CHAR_NUM > 0) index.startIndex - SEARCH_SURROUND_CHAR_NUM else 0
        val lastCutIndex =
            if (index.startIndex + swLength + SEARCH_SURROUND_CHAR_NUM > lastIndex) lastIndex else index.startIndex + swLength + SEARCH_SURROUND_CHAR_NUM
        val surr1 = "..." + wholeString.substring(firstCutIndex, index.startIndex)
        val surr2 = wholeString.substring(index.startIndex + swLength, lastCutIndex) + "..."
        val section = String.format(
            Locale.getDefault(), "%s %s %s",
            surr1,
            sw,
            surr2
        )
        val spanna = SpannableString(section)
        spanna.setSpan(
            BackgroundColorSpan(context.getResources().getColor(R.color.secondary1)),
            surr1.length + 1,
            surr1.length + 1 + sw.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spanna
    }

    private fun searchInString(pageString: String?, sw: String, i: Int): SearchIndex {
        return try {
            searcher!!.searchInString(pageString, sw, i)!!
        } catch (e: Exception) {
            e.printStackTrace()
            SearchIndex(-1, -1)
        }

    }

    fun stopSearch(flag: Boolean) {
        this.flag = flag
    }


    suspend fun searchAndHighlight(resource: String, sw: String): Flow<String> = flow {
        searcher = NormalSearcher()

        if (sw.isNullOrEmpty() && resource.isNullOrEmpty()) {
            emit("")
            return@flow
        }

        val doc: Document = Jsoup.parse(resource)
        val tags: Elements = doc.select("*")

        mSearchCount = 0
        for (tag: Element in tags) {
            for (tn: TextNode in tag.textNodes()) {
                var tagText = tn.text().trim()
                if (tagText.isNotEmpty()) {
                    tn.text(searchAndHighlightNode(tagText, sw))
                }
            }
        }

        emit(StringEscapeUtils.unescapeHtml4(doc.html()))
    }

    private fun searchAndHighlightNode(mNode: String, sw: String): String {

        var node = mNode
        try {
            var searchIndex = searchInString(node, sw, 0)

            while (searchIndex.startIndex >= 0) {
                val swReplacement = node.substring(searchIndex.startIndex, searchIndex.lastIndex)

                ++mSearchCount

                val searchHighlight = addHighlightClass(node, swReplacement, searchIndex, mSearchCount)
                node = searchHighlight.searchHighlight
                searchIndex = searchHighlight.index
                searchIndex = searchInString(node, sw, searchIndex.lastIndex + 1)
            }
            return " $node "
        } catch (e: Exception) {
            e.printStackTrace()
            return " $node "
        }
    }

    private fun addHighlightClass(resource: String, sw: String, index: SearchIndex, searchCount: Int): SearchHighlight {

        val highlight = getHighlightClass(sw, searchCount)
        val nIndex = SearchIndex(index.startIndex, index.startIndex + highlight.length)

        return SearchHighlight(
            String.format(
                Locale.getDefault(), "%s%s%s",
                resource.substring(0, index.startIndex),
                highlight,
                resource.substring(index.lastIndex, resource.length)
            ),
            nIndex
        )
    }

    private fun getHighlightClass(sw: String, searchCount: Int): String {

        return String.format(
            Locale.getDefault(), "<span class=\"search_highlight\" id=\"search_%d\">%s</span>",
            searchCount, sw
        )
    }

}