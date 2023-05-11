package com.mehdok.fineepublib.epubviewer.jsepub.client;

import android.graphics.Picture;
import android.webkit.WebView;

/**
 * Created by mehdok on 9/11/2016.
 */

@SuppressWarnings("deprecation")
public class JsPictureListener implements WebView.PictureListener {
    private WebViewPictureListener webViewPictureListener;

    public JsPictureListener(WebViewPictureListener webViewPictureListener) {
        this.webViewPictureListener = webViewPictureListener;
    }

    @Override
    public void onNewPicture(WebView webView, Picture picture) {
        if (webViewPictureListener != null) {
            webViewPictureListener.onDrawingFinished();
        }
    }

    public interface WebViewPictureListener {
        /**
         * Called when drawing of text finished in WebView
         */
        void onDrawingFinished();
    }

    public void setWebViewPictureListener(
            WebViewPictureListener webViewPictureListener) {
        this.webViewPictureListener = webViewPictureListener;
    }
}
