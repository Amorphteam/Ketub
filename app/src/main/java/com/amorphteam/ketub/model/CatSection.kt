package com.amorphteam.ketub.model

import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class CatSection(val title: String, val des: String, val logo: String) : Parcelable
