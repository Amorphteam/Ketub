package com.amorphteam.ketub.ui.search.searchmode

import com.amorphteam.ketub.model.SearchIndex
import com.amorphteam.ketub.ui.search.BaseSearcher

class NormalSearcher : BaseSearcher() {
    override fun searchInString(input: String?, sw: String?, index: Int): SearchIndex {
        val result = SearchIndex(-1, -1)
        val startIndex = input?.indexOf(sw.toString(), index)
        if (startIndex != null) {
            if (startIndex >= 0) {
                val lastIndex = startIndex + sw!!.length
                result.startIndex = startIndex
                result.lastIndex = lastIndex
            }
        }
        return result
    }


}