package com.amorphteam.ketub.model

import android.text.SpannableString

data class SearchModel(
    val bookAddress: String?,
    val bookTitle: String?,
    val pageId: String?,
    val spanna: SpannableString?,
    val searchCount: Int
)