package com.amorphteam.ketub.ui.main.tabs.tocList

import android.util.Log
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.utility.Keys

class ToclistFragmentViewModel: ViewModel() {
    init {
        Log.i(Keys.LOG_NAME, "load toclist fragment view model")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(Keys.LOG_NAME, "cleared toclist fragment view model")
    }
}