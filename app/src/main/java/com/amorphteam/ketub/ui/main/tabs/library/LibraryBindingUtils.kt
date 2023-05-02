package com.amorphteam.ketub.ui.main.tabs.library

import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.amorphteam.ketub.R
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import com.amorphteam.ketub.utility.Keys
import com.bumptech.glide.Glide


@BindingAdapter("loadImage")
fun setImage(image: ImageView, item: BookModel?) {
        Glide.with(image.context)
            .load(Uri.parse("file:///android_asset/cover/${item!!.bookCover}"))
            .placeholder(R.drawable.ejtihad_logo)
            .into(image)
}
