package com.amorphteam.ketub.ui.main.tabs.index.model

data class IndexModel(
    val id :Int,
    val bookTitle: String,
    val bookName: String
)

data class GroupItem(val id :Int, val bookTitle: String, val bookName: String, val childItems: List<ChildItem>)
data class ChildItem(val id :Int, val bookTitle: String, val bookName: String, val childItems2: List<ChildItem2>)
data class ChildItem2(val id :Int, val bookTitle: String, val bookName: String)
