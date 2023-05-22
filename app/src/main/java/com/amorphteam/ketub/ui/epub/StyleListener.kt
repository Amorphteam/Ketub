package com.amorphteam.ketub.ui.epub

import com.amorphteam.ketub.model.Theme

interface StyleListener {
    fun changeFontSize(fontSize: Int?)
    fun changeLineSpace(lineSpace: Int?)
    fun changeFontName(font: Int?)
    fun changeBkColor(bkColor: Int?)
    fun changeTheme(theme: Int?)
}