package com.amorphteam.ketub.utility

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.amorphteam.ketub.model.BookHolder
import com.amorphteam.ketub.ui.epub.EpubActivity
import com.amorphteam.ketub.ui.epub.EpubVerticalDelegate
import com.mehdok.fineepublib.epubviewer.epub.Book


class EpubHelper() {
    companion object{
        fun openEpub(bookAddress: String, context: Context){
            val intent = Intent(context, EpubActivity::class.java)
            intent.putExtra(Keys.BOOK_ADDRESS, bookAddress)
            context.startActivity(intent)
        }

        fun openEpub(bookAddress: String, pageIndex: Int, context: Context) {  // To avoid to open new activity again
            if (isEpubActivityRunning(context)) {
                EpubVerticalDelegate.get()?.activity?.onBackPressed()
                EpubVerticalDelegate.get()?.activity?.binding?.epubVerticalViewPager?.setCurrentItem(pageIndex, false)
            } else {
                val intent = Intent(context, EpubActivity::class.java)
                intent.putExtra(Keys.BOOK_ADDRESS, bookAddress)
                intent.putExtra(Keys.NAV_INDEX, pageIndex)
                context.startActivity(intent)
            }
        }
        fun openEpub(bookAddress: String, pageUri: String, context: Context){

            if (isEpubActivityRunning(context)) { // To avoid to open new activity again
                EpubVerticalDelegate.get()?.activity?.onBackPressed()
                val pageIndex =
                    BookHolder.instance?.jsBook?.getResourceNumber(Book.resourceName2Url(pageUri))
                EpubVerticalDelegate.get()?.activity?.binding?.epubVerticalViewPager?.setCurrentItem(
                    pageIndex!!, false)
            } else {
                val intent = Intent(context, EpubActivity::class.java)
                intent.putExtra(Keys.BOOK_ADDRESS, bookAddress)
                intent.putExtra(Keys.NAV_URI, pageUri)
                context.startActivity(intent)
            }
        }


        fun openEpub(bookAddress: String, pageUri: String, searchWord:String, context: Context){
            val intent = Intent(context, EpubActivity::class.java)
            intent.putExtra(Keys.BOOK_ADDRESS, bookAddress)
            intent.putExtra(Keys.NAV_URI, pageUri)
            intent.putExtra(Keys.SEARCH_WORD, searchWord)
            context.startActivity(intent)
        }

        fun getBookAddressFromBookPath(bookPath: String, context: Context): String? {
            val fileManager = FileManager(context)
            return fileManager.getBookAddress(bookPath)
        }

        fun getContentWithoutTag(mContent: String): String {
            val indexOf = mContent.indexOf('#')
            var temp = mContent
            if (0 < indexOf) {
                temp = mContent.substring(0, indexOf)
            }
            return temp
        }

        fun isContainerForAllBooks(catName: String, singleBookName: String):Boolean{
            var forAllBooks = false

            if (catName.isEmpty()) forAllBooks = false
            if (singleBookName.isEmpty()) forAllBooks = true
            return forAllBooks
        }

        private fun isEpubActivityRunning(context: Context): Boolean {
            val intent = Intent(context, EpubActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            return pendingIntent == null
        }


    }
}