package com.amorphteam.ketub.ui.main.tabs.index.model


data class IndexGroupItem(val id :Int, val bookTitle: String, val bookName: String, val childItems: List<IndexFirstChildItem>)
data class IndexFirstChildItem(val id :Int, val bookTitle: String, val bookName: String, val childItems2: List<IndexSecondChildItem>)
data class IndexSecondChildItem(val id :Int, val bookTitle: String, val bookName: String)
