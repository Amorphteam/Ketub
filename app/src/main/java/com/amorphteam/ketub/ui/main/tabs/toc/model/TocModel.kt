package com.amorphteam.ketub.ui.main.tabs.toc.model


data class TocGroupItem(val id :Int, val bookTitle: String, val bookName: String, val childItems: List<TocFirstChildItem>)
data class TocFirstChildItem(val id :Int, val bookTitle: String, val bookName: String, val childItems2: List<TocSecondChildItem>)
data class TocSecondChildItem(val id :Int, val bookTitle: String, val bookName: String)
