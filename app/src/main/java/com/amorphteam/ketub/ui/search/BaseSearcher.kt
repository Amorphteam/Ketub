package com.amorphteam.ketub.ui.search

import com.amorphteam.ketub.model.SearchIndex

abstract class BaseSearcher {
    abstract fun searchInString(input: String?, sw: String?, index: Int): SearchIndex?
}
