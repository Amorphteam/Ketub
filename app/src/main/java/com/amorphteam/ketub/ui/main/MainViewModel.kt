package com.amorphteam.ketub.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    var text =  "saman"
    var startEpubViewer = MutableLiveData<Boolean>()


    override fun onCleared() {
        super.onCleared()
    }

    init {

    }

    fun startEpubViewer (){
        startEpubViewer.value = true
    }

}