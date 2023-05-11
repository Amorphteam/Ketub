package com.mehdok.fineepublib.epubviewer.util;

import android.content.Context;
import android.util.Log;
import android.webkit.WebResourceResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mehdok on 9/11/2016.
 */

public class BookAssetUtil {
    public static final String STYLE_FOLIO = "css/MehdokStyle.css";
    public static final String JS_FOLIO = "js/MehdokBridge.js";

    private static BookAssetUtil Instance;

    public static BookAssetUtil getInstance() {
        if (Instance == null) {
            Instance = new BookAssetUtil();
        }

        return Instance;
    }

    private BookAssetUtil() {

    }

    public WebResourceResponse getCssForName(Context context, String cssName) {
        WebResourceResponse webResponse = new WebResourceResponse("text/css", "UTF-8", null);
        try
        {
            InputStream css = null;
            css = context.getAssets().open(cssName);
            webResponse.setData(css);
        }
        catch (IOException e)
        {
            Log.e("getStyleFromFile", "can not find style file - stackTrace: " + e.getMessage().toString());
            return null;
        }

        return webResponse;
    }

    public WebResourceResponse getFontFroName(Context context, String fontName, String mimeType) {
        InputStream font = null;
        try {
            font = context.getAssets().open(fontName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (font == null) {
            return null;
        } else {
            return new WebResourceResponse(mimeType, "UTF-8", font);
        }
    }

    synchronized public WebResourceResponse getJsForName(Context context, String jsName) {
        WebResourceResponse webResponse = new WebResourceResponse("text/javascript", "UTF-8", null);
        try {
            InputStream css = null;
            css = context.getAssets().open(jsName);
            webResponse.setData(css);
        } catch (IOException e) {
            Log.e("getStyleFromFile", "can not find style file - stackTrace: " + e.getMessage().toString());
            return null;
        }

        return webResponse;

    }

    private static String getAssetFileAsString(Context ctx, String address) {
        try {
            StringBuilder buffer = new StringBuilder();
            InputStream text = ctx.getAssets().open(address);
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(text, "UTF-8"));
            String str;

            while ((str = in.readLine()) != null) {
                buffer.append(str);
            }

            in.close();

            return new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
