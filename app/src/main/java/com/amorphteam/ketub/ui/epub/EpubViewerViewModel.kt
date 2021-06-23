package com.amorphteam.ketub.ui.epub


import android.util.Log
import android.webkit.WebView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.utility.Keys.Companion.LOG_NAME


class EpubViewerViewModel : ViewModel() {

    val webViewUrl = MutableLiveData<String>().apply { value = "file:///android_asset/sample.html" }
    val defaultStyleStatus = MutableLiveData<Boolean>().apply { value = true }
    val moreReadabilityStyleStatus = MutableLiveData<Boolean>().apply { value = false }
    val highContrastStyleStatus = MutableLiveData<Boolean>().apply { value = false }
    val darkModeStyleStatus = MutableLiveData<Boolean>().apply { value = false }
    val basetTheme = MutableLiveData<Boolean>().apply { value = true }
    val darkTheme = MutableLiveData<Boolean>().apply { value = false }
    val lightTheme = MutableLiveData<Boolean>().apply { value = false }

    val fontSizeProgress = MutableLiveData<Int>().apply { if (defaultStyleStatus.value!!) value = 2 }
    val lineHightProgress = MutableLiveData<Int>().apply { if (defaultStyleStatus.value!!) value = 2 }

    init {
        Log.i(LOG_NAME, "load epub viewer view model")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(LOG_NAME, "cleared epub viewer view model")
    }

    fun onClickBaseTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        basetTheme.value = true
        lightTheme.value = false
        darkTheme.value = false
    }

    fun onClickDarkTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        basetTheme.value = false
        lightTheme.value = false
        darkTheme.value = true
    }

    fun onClickLightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        basetTheme.value = false
        lightTheme.value = true
        darkTheme.value = false
    }

    fun onClickOnDefaultStyle() {
        defaultStyleStatus.value = true
        highContrastStyleStatus.value = false
        moreReadabilityStyleStatus.value = false
        darkModeStyleStatus.value = false
        fontSizeProgress.value = 2
        lineHightProgress.value = 2
    }

    fun onClickOnMoreReadabilityStyle() {
        defaultStyleStatus.value = false
        highContrastStyleStatus.value = false
        moreReadabilityStyleStatus.value = true
        darkModeStyleStatus.value = false
        fontSizeProgress.value = 6
        lineHightProgress.value = 6
    }

    fun onClickOnDarkModeStyle() {
        defaultStyleStatus.value = false
        highContrastStyleStatus.value = false
        moreReadabilityStyleStatus.value = false
        darkModeStyleStatus.value = true
        fontSizeProgress.value = 0
        lineHightProgress.value = 0
        onClickDarkTheme()
    }

    fun updateFontSizeSeekerBar(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        fontSizeProgress.value = progress
        Log.i(LOG_NAME, "fontSize$progress")
    }

    fun updateLineHightSeekerBar(seekBar: SeekBar, progress: Int, fromUser: Boolean){
        lineHightProgress.value = progress
        Log.i(LOG_NAME, "lineHeight$progress")
    }

    fun onClickOnHighContrast() {
        defaultStyleStatus.value = false
        highContrastStyleStatus.value = true
        moreReadabilityStyleStatus.value = false
        darkModeStyleStatus.value = false
        fontSizeProgress.value = 3
        lineHightProgress.value = 2
    }


    companion object {
        @JvmStatic
        @BindingAdapter("loadUrl")
        fun WebView.setUrl(url: String) {
            this.loadUrl(url)
        }
    }
}