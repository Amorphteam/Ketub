package com.amorphteam.ketub.ui.search.searchmode

import com.amorphteam.ketub.model.SearchIndex
import com.amorphteam.ketub.ui.search.BaseSearcher
import java.text.Collator
import java.util.*

class WordByWordDiacriticInSensitiveSearcher : BaseSearcher() {
    override fun searchInString(input: String?, sw: String?, index: Int): SearchIndex {
        var input = input
        val result = SearchIndex(-1, -1)
        var counter = index
        input = input?.substring(index, input.length)
        val instance = Collator.getInstance(Locale("ar"))
        instance.strength = Collator.NO_DECOMPOSITION
        val source = input?.split(" ".toRegex())?.dropLastWhile { it.isEmpty() }
            ?.toTypedArray()
        val target = sw?.split(" ".toRegex())?.dropLastWhile { it.isEmpty() }
            ?.toTypedArray()
        if (source != null) {
            for (i in source.indices) {
                if (instance.compare(source[i], target!![0]) == 0) {
                    //first char is equal
                    if (target.size > 1) {
                        // check other char
                        val exist = searchForCompanionWords(instance, source, target, i, 0)
                        if (exist) {
                            result.startIndex = counter
                            result.lastIndex = getLastIndex(source, i, target.size, counter)
                        }
                    } else {
                        result.startIndex = counter
                        result.lastIndex = counter + source[i].length
                        return result
                    }
                }
                counter += source[i].length + 1 // +1 for omitted space
            }
        }
        return result
    }

    private fun getLastIndex(source: Array<String>, start: Int, howMany: Int, size: Int): Int {
        var size = size
        for (i in start until start + howMany) {
            size += source[i].length + 1 // +1 for omitted space
        }
        return size - 1 // -1 for last extra space
    }

    private fun searchForCompanionWords(
        collator: Collator, source: Array<String>, target: Array<String>, sourceMatch: Int,
        targetMatch: Int
    ): Boolean {
        if (source.size <= sourceMatch + 1) return false
        if (target.size <= targetMatch + 1) return false
        return if (collator.compare(source[sourceMatch + 1], target[targetMatch + 1]) == 0) {
            // The next char matches, is there any target word?
            if (target.size <= targetMatch + 2) {
                true
            } else {
                // There is more target word
                searchForCompanionWords(collator, source, target, sourceMatch + 1, targetMatch + 1)
            }
        } else false
    }
}
