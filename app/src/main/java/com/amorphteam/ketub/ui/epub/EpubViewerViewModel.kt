package com.amorphteam.ketub.ui.epub

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.utility.Keys.Companion.LOG_NAME
import kotlinx.android.synthetic.main.activity_epub_viewer.*

class EpubViewerViewModel : ViewModel() {
    val webViewUrl = MutableLiveData<String>().apply { value = "file:///android_asset/sample.html" }

    init {
        Log.i(LOG_NAME, "load epub viewer view model")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(LOG_NAME, "cleared epub viewer view model")
    }



    companion object {
        @JvmStatic
        @BindingAdapter("loadUrl")
        fun WebView.setUrl(url: String) {
            this.loadUrl(url)
        }
    }
}