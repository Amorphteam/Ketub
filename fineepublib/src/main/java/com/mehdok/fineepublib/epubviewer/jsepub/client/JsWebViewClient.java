package com.mehdok.fineepublib.epubviewer.jsepub.client;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by mehdok on 9/11/2016.
 */

public class JsWebViewClient extends WebViewClient {
    private WebViewClientListener webViewClientListener;

    public JsWebViewClient(WebViewClientListener webViewClientListener) {
        this.webViewClientListener = webViewClientListener;
    }

    @SuppressWarnings("deprecation")
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        if (url.contains("localhost")) {
            return webViewClientListener.onRequest(url);
        } else {
            return null;//new WebResourceResponse(url, url, null);
        }
        /*if (webViewClientListener != null) {
            return webViewClientListener.onRequest(url);
        }

        return null;*/
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WebResourceResponse shouldInterceptRequest(WebView view,
                                                      WebResourceRequest request) {
        if (request.getUrl().toString().contains("localhost")) {
            return webViewClientListener.onRequest(request.getUrl().toString());
        } else {
            return null;
        }
        /*if (webViewClientListener != null) {
            return webViewClientListener.onRequest(request.getUrl().toString());
        }

        return null;*/
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (webViewClientListener != null) {
            webViewClientListener.onPageFinished();
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (webViewClientListener != null) {
            webViewClientListener.onPageStarted();
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (webViewClientListener != null) {
            return webViewClientListener.shouldOverrideUrlLoading(view, url);
        }

        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (webViewClientListener != null) {
            return webViewClientListener.shouldOverrideUrlLoading(view, request);
        }

        return super.shouldOverrideUrlLoading(view, request);
    }

    public void setWebViewClientListener(
            WebViewClientListener webViewClientListener) {
        this.webViewClientListener = webViewClientListener;
    }


}
