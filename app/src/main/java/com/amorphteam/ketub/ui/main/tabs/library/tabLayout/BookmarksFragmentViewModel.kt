package com.amorphteam.ketub.ui.main.tabs.library.tabLayout

import android.util.Log
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.utility.Keys

class BookmarksFragmentViewModel: ViewModel() {
    init {
        Log.i(Keys.LOG_NAME, "load bookmarks fragment view model")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(Keys.LOG_NAME, "cleared bookmarks fragment view model")
    }
}