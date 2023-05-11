package com.amorphteam.ketub.utility

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class FileManager(context: Context) {

    private var BASE_OUT_ADDRESS: String? = null
    private var OUT_BOOK_ADDRESS: String? = null
    private var OUT_COVER_ADDRESS: String? = null

    init {
        BASE_OUT_ADDRESS = SDUtil.getWorkingDirectory(context)
        if (BASE_OUT_ADDRESS == null) {
            throw RuntimeException("WE CAN NOT COPY FILE TO YOUR PHONE")
        }

        BASE_OUT_ADDRESS = context.getExternalFilesDir(null)!!.absolutePath
        OUT_BOOK_ADDRESS = "$BASE_OUT_ADDRESS/books"
        OUT_COVER_ADDRESS = "$BASE_OUT_ADDRESS/covers"

        // create this directories
        val base = File(BASE_OUT_ADDRESS)
        base.mkdirs()
        val book = File(OUT_BOOK_ADDRESS)
        book.mkdirs()
        val cover = File(OUT_COVER_ADDRESS)
        cover.mkdirs()
    }

    private fun isNewVersion(context: Context?): Boolean {
        val manager = context?.let { PreferencesManager(it) }
        val currVersion: Int = manager!!.loadAppVersion()

        // if 0, first run
        val nVersion: Int = getAppCurrentVersion(context)
        return nVersion > currVersion
    }

    private fun getAppCurrentVersion(context: Context): Int {
        var pInfo: PackageInfo? = null
        try {
            pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return pInfo!!.versionCode
    }

    private fun deleteOldBook(destination: String): Boolean {
        val exist: Boolean = FileAction.isExist(destination)
        return if (exist) {
            FileAction.deleteFile(destination)
        } else {
            true
        }
    }

    fun copyBooksToUserDoc(context: Context?) {
        if (isNewVersion(context)) {
            if (deleteOldBook(OUT_BOOK_ADDRESS!!)) {
                if (context != null) {
                    copyNewBookToUserDoc(context)
                }
            }
        }
    }

    fun copyCoversToUSerDoc(context: Context?) {
        if (isNewVersion(context)) {
            if (deleteOldBook(OUT_COVER_ADDRESS!!)) {
                if (context != null) {
                    copyNewCoversToUserDoc(context)
                }
            }
        }
    }

    private fun copyNewBookToUserDoc(context: Context) {
        OUT_BOOK_ADDRESS?.let { copyAllFileFromAssetToUserDoc(context, Keys.ASSET_BOOK_DIR, it) }
    }

    private fun copyNewCoversToUserDoc(context: Context) {
        OUT_COVER_ADDRESS?.let { copyAllFileFromAssetToUserDoc(context, Keys.ASSET_COVER_DIR, it) }
    }

    private fun copyAllFileFromAssetToUserDoc(
        context: Context,
        assetPath: String,
        outAddress: String
    ) {
        val assetManager = context.assets
        var files: Array<String>? = null
        var `in`: InputStream? = null
        var out: OutputStream? = null
        try {
            files = assetManager.list(assetPath)

        } catch (e: IOException) {

            Log.i(Keys.LOG_NAME, "Failed to get asset file list: $assetPath", e)

        }

        val dir = File(outAddress)
        if (dir.mkdirs() && files != null) {
            for (filename in files) {
                try {
                    `in` = assetManager.open("$assetPath/$filename")
                    val outFile = File(outAddress, filename)
                    out = FileOutputStream(outFile)
                    copyFile(`in`, out)
                    `in`.close()
                    `in` = null
                    out.flush()
                    out.close()
                    out = null
                } catch (e: IOException) {
                    Log.i(Keys.LOG_NAME, "Failed to copy asset file: $filename", e)
                    return
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun copyFile(`in`: InputStream, out: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        while (`in`.read(buffer).also { read = it } != -1) {
            out.write(buffer, 0, read)
        }
    }


    fun getCoverUri(coverName: String): Uri? {
        val coverFile = File("$OUT_COVER_ADDRESS/$coverName")
        return Uri.fromFile(coverFile)
    }

    fun getCoverByte(coverName: String): ByteArray? {
        val coverFile = File("$OUT_COVER_ADDRESS/$coverName")
        try {
            return coverFile.readBytes()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ByteArray(0)
    }


    fun getBooksAddress(booksName: Array<String>): Array<String?>? {
        val result = arrayOfNulls<String>(booksName.size)
        for (i in booksName.indices) {
            result[i]?.let { getBookAddress(it) }
        }

        return result
    }

    private fun getBookAddress(booksName: String): String? {
        return if (!booksName.contains(OUT_BOOK_ADDRESS!!)) {
            "$OUT_BOOK_ADDRESS/$booksName"
        } else booksName
    }

}