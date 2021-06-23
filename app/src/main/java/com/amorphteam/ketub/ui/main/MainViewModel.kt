package com.amorphteam.ketub.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.utility.Keys.Companion.LOG_NAME

class MainViewModel: ViewModel() {
    var startEpubAct = MutableLiveData<Boolean>()
    init {
        Log.i(LOG_NAME, "main view model created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(LOG_NAME, "main view model was cleared")
    }

    fun openEpubAct(){
        startEpubAct.value = true
    }
}