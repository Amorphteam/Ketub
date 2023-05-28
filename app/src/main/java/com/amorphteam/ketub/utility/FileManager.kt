package com.amorphteam.ketub.utility

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import com.amorphteam.ketub.R
import java.io.File
import java.io.FileOutputStream
import java.io.FilenameFilter
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class FileManager(context: Context) {
    private var BASE_OUT_ADDRESS: String? = null
    private var OUT_BOOK_ADDRESS: String? = null
    private var OUT_COVER_ADDRESS: String? = null

    init {
        BASE_OUT_ADDRESS = getWorkingDirectory(context)
        if (BASE_OUT_ADDRESS == null) {
            throw RuntimeException(context.getString(R.string.error_copy_file))
        }

        BASE_OUT_ADDRESS = context.getExternalFilesDir(null)!!.getAbsolutePath()

        OUT_BOOK_ADDRESS = "$BASE_OUT_ADDRESS/${Keys.ASSET_BOOK_DIR}"
        OUT_COVER_ADDRESS = "$BASE_OUT_ADDRESS/${Keys.ASSET_COVER_DIR}"

        // create this directories
        val base = File(BASE_OUT_ADDRESS)
        base.mkdirs()
        val book = File(OUT_BOOK_ADDRESS)
        book.mkdirs()
        val cover = File(OUT_COVER_ADDRESS)
        cover.mkdirs()
    }

    fun isNewVersion(context: Context): Boolean {
        val pref = PreferencesManager(context)
        val currVersion: Int = pref!!.loadAppVersion()

        // if 0, first run
        val nVersion: Int = getAppCurrentVersion(context)
        return nVersion > currVersion
    }

    fun saveAppVersion(context: Context){
        val pref = PreferencesManager(context)
        pref.saveAppVersion(getAppCurrentVersion(context))
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
        return if (isExist(destination)) {
            deleteFile(destination)
        } else {
            true
        }
    }

    fun copyBooksToUserDoc(context: Context) {
        if (isNewVersion(context)) {
            if (deleteOldBook(OUT_BOOK_ADDRESS!!)) {
                copyNewBooksToUserDoc(context)
            }
        }
    }

    fun copyCoversToUSerDoc(context: Context) {
        if (isNewVersion(context)) {
            if (deleteOldBook(OUT_COVER_ADDRESS!!)) {
                copyNewCoversToUserDoc(context)
            }
        }
    }

    private fun copyNewBooksToUserDoc(context: Context) {
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

    fun getBookAddress(booksName: String): String? {
        return if (!booksName.contains(OUT_BOOK_ADDRESS!!)) {
            "$OUT_BOOK_ADDRESS/$booksName"
        } else booksName
    }

    private fun getWorkingDirectory(context: Context): String? {
        val externalDir = context.getExternalFilesDir(null)
        val internalDir = context.filesDir
        return if (externalDir != null) {
            externalDir.absolutePath
        } else
            internalDir?.absolutePath
    }

    private fun isWriteable(currentPath: String): Boolean {
        val file = createFile(currentPath)
        return file.canWrite()
    }

    private fun isReadable(currentPath: String): Boolean {
        val file = createFile(currentPath)
        return file.canRead()
    }

    private fun createFile(currentPath: String): File {
        return File(currentPath)
    }

    private fun deleteFile(currentPath: String): Boolean {
        if (checkFile(currentPath) && isReadable(currentPath) && isWriteable(currentPath)) {
            val file = createFile(currentPath)
            return file.delete()
        } else if (checkDirectory(currentPath) &&
            isReadable(currentPath) &&
            isWriteable(currentPath)
        ) {
            val items = getItemList(currentPath, true)
            if (items.isEmpty()) {
                val file = createFile(currentPath)
                return file.delete()
            } else if (items.isNotEmpty()) {
                for (i in items.indices) {
                    val tempPath = currentPath + File.separatorChar + items[i]
                    if (checkDirectory(tempPath)) {
                        deleteFile(tempPath)
                    } else if (checkFile(tempPath)) {
                        val file = createFile(tempPath)
                        file.delete()
                    }
                }
            }
            val file = createFile(currentPath)
            return file.delete()
        }
        return false
    }

    private fun checkFile(currentPath: String): Boolean {
        val file = createFile(currentPath)
        return file.isFile
    }

    private fun checkDirectory(currentPath: String): Boolean {
        val file = createFile(currentPath)
        return file.isDirectory
    }

    private fun getItemList(currentPath: String, showHiddenFile: Boolean): Array<String> {
        return if (showHiddenFile) {
            val file = createFile(currentPath)
            val itemList = file.list()
            itemList
        } else {
            val filter = FilenameFilter { dir, fileName -> !fileName.startsWith(".") }
            val file = createFile(currentPath)
            file.list(filter)
        }
    }

    private fun isExist(path: String?): Boolean {
        val file = File(path)
        return file.exists()
    }
}