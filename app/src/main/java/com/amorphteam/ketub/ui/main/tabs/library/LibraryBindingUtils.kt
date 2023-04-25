package com.amorphteam.ketub.ui.main.tabs.library

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import com.bumptech.glide.Glide


@BindingAdapter("image", "placeholder", "item")
fun setImage(image: ImageView, url: String?, placeHolder: Drawable, item: BookModel?) {

    if (!url.isNullOrEmpty()) {
        Glide.with(image.context)
            .load(Uri.parse("file:///android_asset/cover/${item!!.bookCover}"))
            .into(image)
    } else {
        image.setImageDrawable(placeHolder)
    }

}
