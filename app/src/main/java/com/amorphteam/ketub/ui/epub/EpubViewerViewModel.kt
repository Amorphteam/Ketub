package com.amorphteam.ketub.ui.epub

import android.webkit.WebView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EpubViewerViewModel: ViewModel() {

    val webViewUrl = MutableLiveData<String>().apply { value = "file:///android_asset/sample.html" }

    init {

    }

    companion object {
        @JvmStatic
        @BindingAdapter("loadUrl")
        fun WebView.setUrl(url: String) {
            this.loadUrl(url)
        }
    }

}