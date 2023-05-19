package com.amorphteam.ketub.model

import android.text.SpannableString

data class SearchInfoHolder(
    val bookAddress: String?,
    val bookTitle: String?,
    val pageId: String?,
    val spanna: SpannableString?,
    val searchCount: Int
)