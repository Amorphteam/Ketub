package com.mehdok.fineepublib.epubviewer.jsepub.client;

import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

/**
 * Created by mehdok on 9/11/2016.
 */

public interface WebViewClientListener {
    /**
     * Each time webview make an request this method will call, it look for url scheme, if it starts with
     * "localhost" it will search trough epub and will return the response if resource exist and null otherwise
     *
     * @param url
     * @return {@link WebResourceResponse} containing epub resource
     */
    WebResourceResponse onRequest(String url);

    /**
     * Called when page finished it's loading, finishing loading does NOT mean drawing finished, to know when
     * drawing is finished {@link WebView.PictureListener} must be used
     */
    void onPageFinished();

    /**
     * Called when loading of page getting started
     */
    void onPageStarted();

    boolean shouldOverrideUrlLoading(WebView view, String url);

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request);
}
