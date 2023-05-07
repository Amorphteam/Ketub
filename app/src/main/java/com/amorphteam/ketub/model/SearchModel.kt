package com.amorphteam.ketub.model

data class SearchModel(
    val id: Int,
    var bookPage: Int,
    val bookName: String,
    var searchResult: String
)
