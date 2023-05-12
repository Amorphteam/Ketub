package com.amorphteam.ketub.model

import com.mehdok.fineepublib.epubviewer.epub.Book
import com.mehdok.fineepublib.epubviewer.jsepub.JSBook

class BookHolder {
    var book: Book? = null
    var jsBook: JSBook? = null

    companion object {
        private var singleton: BookHolder? = null
        val instance: BookHolder?
            get() {
                if (singleton == null) {
                    singleton = BookHolder()
                }
                return singleton
            }
    }
}
