package com.amorphteam.ketub.ui.search.searchmode

import com.amorphteam.ketub.model.SearchIndex
import com.amorphteam.ketub.ui.search.BaseSearcher

class DiacriticSensitiveSearcher : BaseSearcher() {
    override fun searchInString(input: String?, sw: String?, index: Int): SearchIndex? {
        val result = SearchIndex(-1, -1)
        var counter = 0
        var temp: String
        if (input != null) {
            for (i in index until input.length) {
                temp = removeDiacritic(input.substring(i, i + 1))
                if (temp == "") continue  // if it is empty it means the char is diacritic
                if (sw?.length!! - 1 == counter) {
                    //the char found
                    result.lastIndex = i + 1 // This is the last index
                    return result
                }
                if (temp == sw.substring(counter, counter + 1)) {
                    // the first char is equal, search for other
                    if (counter == 0) {
                        result.startIndex = i
                    }
                    ++counter
                } else {
                    // reset char comparation
                    counter = 0
                }
            }
        }
        result.startIndex = -1
        result.lastIndex = -1
        return result
    }

    companion object {
        private fun removeDiacritic(input: String): String {
            var input = input
            input = input.replace("\u0650".toRegex(), "")
            input = input.replace("\u0651".toRegex(), "")
            input = input.replace("\u0652".toRegex(), "")
            input = input.replace("\u064E".toRegex(), "")
            input = input.replace("\u064F".toRegex(), "")
            input = input.replace("\u064D".toRegex(), "")
            input = input.replace("\u064B".toRegex(), "")
            return input
        }
    }
}