package com.amorphteam.ketub.ui.epub.fragments

interface StyleListener {
    fun changeFontSize(fontSize: Int?)
    fun changeLineSpace(lineSpace: Int?)
    fun changeFontName(font: Int?)
    fun changeBkColor(BkColor: Int?)
}