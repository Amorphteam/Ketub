package com.amorphteam.ketub.ui.main.fragments

import android.util.Log
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.utility.Keys

class TitleFragmentViewModel: ViewModel() {
    init {
        Log.i(Keys.LOG_NAME, "load title fragment view model")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(Keys.LOG_NAME, "cleared title fragment view model")
    }
}