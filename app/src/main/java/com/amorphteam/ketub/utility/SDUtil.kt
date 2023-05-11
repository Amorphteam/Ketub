package com.amorphteam.ketub.utility

import android.content.Context

object SDUtil {
    fun getWorkingDirectory(context: Context): String? {
        val externalDir = context.getExternalFilesDir(null)
        val internalDir = context.filesDir
        return if (externalDir != null) {
            externalDir.absolutePath
        } else
            internalDir?.absolutePath
    }
}