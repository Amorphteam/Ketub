package com.amorphteam.ketub.utility

import android.content.Context
import android.content.Intent
import com.amorphteam.ketub.ui.epub.EpubActivity

class EpubHelper() {
    companion object{
        fun openEpub(bookAddress: String, context: Context,){
            val intent = Intent(context, EpubActivity::class.java)
            intent.putExtra(Keys.BOOK_ADDRESS, bookAddress)
            context.startActivity(intent)
        }

        fun openEpub(bookAddress: String, pageIndex: Int, context: Context){
            val intent = Intent(context, EpubActivity::class.java)
            intent.putExtra(Keys.BOOK_ADDRESS, bookAddress)
            intent.putExtra(Keys.NAV_INDEX, pageIndex)
            context.startActivity(intent)
        }

        fun openEpub(bookAddress: String, pageUri: String, context: Context){
            val intent = Intent(context, EpubActivity::class.java)
            intent.putExtra(Keys.BOOK_ADDRESS, bookAddress)
            intent.putExtra(Keys.NAV_URI, pageUri)
            context.startActivity(intent)
        }

        fun getBookAddressFromBookPath(bookPath: String, context: Context): String? {
            val fileManager = FileManager(context)
            return fileManager.getBookAddress(bookPath)
        }

    }
}