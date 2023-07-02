package com.amorphteam.ketub.model

import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CatSection(val title: String, val des: String, val logo: String) : Parcelable
