package com.amorphteam.ketub.utility

import java.io.File
import java.io.FilenameFilter

object FileAction {

    fun isWriteable(currentPath: String): Boolean {
        val file = createFile(currentPath)
        return file.canWrite()
    }

    fun isReadable(currentPath: String): Boolean {
        val file = createFile(currentPath)
        return file.canRead()
    }

    private fun createFile(currentPath: String): File {
        return File(currentPath)
    }

    fun deleteFile(currentPath: String): Boolean {
        if (checkFile(currentPath) && isReadable(currentPath) && isWriteable(currentPath)) {
            val file = createFile(currentPath)
            return file.delete()
        } else if (checkDirectory(currentPath) &&
            isReadable(currentPath) &&
            isWriteable(currentPath)
        ) {
            val items = getItemList(currentPath, true)
            if (items.size == 0) {
                val file = createFile(currentPath)
                return file.delete()
            } else if (items.size > 0) {
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

    fun checkFile(currentPath: String): Boolean {
        val file = createFile(currentPath)
        return file.isFile
    }

    fun checkDirectory(currentPath: String): Boolean {
        val file = createFile(currentPath)
        return file.isDirectory
    }

    fun getItemList(currentPath: String, showHiddenFile: Boolean): Array<String> {
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

    fun isExist(path: String?): Boolean {
        val file = File(path)
        return file.exists()
    }
}
