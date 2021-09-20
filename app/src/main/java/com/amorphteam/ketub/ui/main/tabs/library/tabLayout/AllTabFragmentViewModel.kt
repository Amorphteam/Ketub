package com.amorphteam.ketub.ui.main.tabs.library.tabLayout

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.utility.Keys

class AllTabFragmentViewModel: ViewModel() {
    var startEpubAct = MutableLiveData<Boolean>()
    init {
        Log.i(Keys.LOG_NAME, "main view model created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(Keys.LOG_NAME, "main view model was cleared")
    }

    fun openEpubAct(){
        startEpubAct.value = true
    }
}