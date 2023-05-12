package com.amorphteam.ketub.utility

import android.content.Context
import android.util.Log
import android.webkit.WebResourceResponse
import java.io.IOException
import java.io.InputStream

class AssetUtil private constructor() {
    fun getCssForName(context: Context, cssName: String?): WebResourceResponse? {
        val webResponse = WebResourceResponse("text/css", "UTF-8", null)
        try {
            var css: InputStream? = null
            css = context.assets.open(cssName!!)
            webResponse.data = css
        } catch (e: IOException) {
            Log.e(
                "getStyleFromFile",
                "can not find style file - stackTrace: " + e.message.toString()
            )
            return null
        }
        return webResponse
    }

    fun getFontFroName(
        context: Context,
        fontName: String?,
        mimeType: String?
    ): WebResourceResponse? {
        var font: InputStream? = null
        try {
            font = context.assets.open(fontName!!)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return if (font == null) {
            null
        } else {
            WebResourceResponse(mimeType, "UTF-8", font)
        }
    }

    companion object {
        var instance: AssetUtil? = null
            get() {
                if (field == null) {
                    field = AssetUtil()
                }
                return field
            }
            private set
    }
}