/*******************************************************************************
 * Programmer : Mehdi Sohrabi Email : mehdok@gmail.com
 ******************************************************************************/
package com.mehdok.fineepublib.epubviewer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/*
 * EpubWebView for use with Android 3.0 and above
 * The class uses shouldInterceptRequest() to load
 * resources that are referenced (i.e. Links) into the
 * view.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class EpubWebView30 extends EpubWebView {
    public EpubWebView30(Context context) {
        super(context, null);
    }

    public EpubWebView30(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
     *  Do any Web settings specific to the derived class 
     */
    protected void addWebSettings(WebSettings settings) {
        settings.setDisplayZoomControls(false);
    }

    /*
     * @ return Android version appropriate WebViewClient 
     */
    @Override
    protected WebViewClient createWebViewClient() {
        return new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (url.contains("localhost")) {
                    return onRequest(url);
                } else {
                    return null;//new WebResourceResponse(url, url, null);
                }
            }

            @TargetApi(19)
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                reloadFlag = true;
                if (resetScale) {
                    resetScale = false;
                } else {
                    view.setInitialScale((int) (100 * view.getScale()));
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadJQuery();
                setBackgroundColor();
                applyFontLineSpaceChange();
            }

            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
            }
        };
    }

    @Override
    protected PictureListener createPictureListener() {
        return new EpubPictureListener();
    }

    protected void LoadUri(Uri uri) {
        loadUrl(uri.toString());
    }

    /*
      * Called when Android 3.0 webview wants to load a URL.
      * @return the requested resource from the ebook
      */
    private WebResourceResponse onRequest(String url) {
        if (isNotNull(epubListener)) {
            return epubListener.onUrlRequest(url);
        } else {
            getWebViewClient().onReceivedError(this, WebViewClient.ERROR_FILE_NOT_FOUND,
                                               "Unable to find resource in epub", url);
            return new WebResourceResponse("", "UTF-8", null);
        }
    }

    /**
     * inject jQuery lib and my script to head of document
     */
    private void loadJQuery() {
        String jq =
                "javascript:(function(){var oHead = document.getElementsByTagName('head').item(0); var oScript= document.createElement('script'); oScript.type = \"text/javascript\"; oScript.src= " +
                        jQueryHandler.getJQueryLib() +
                        "; oHead.appendChild(oScript);}())";
        exeJS(jq);

        jq =
                "javascript:(function(){var oHead = document.getElementsByTagName('head').item(0); var oScript= document.createElement('script'); oScript.type = \"text/javascript\"; oScript.src= " +
                        jQueryHandler.getLocalJqueryScript() +
                        "; oHead.appendChild(oScript);}())";
        exeJS(jq);
    }

    @SuppressWarnings("deprecation")
    private class EpubPictureListener implements PictureListener {

        @Override
        public void onNewPicture(WebView view, Picture arg1) {
            stopLoadingIcon();
        }
    }
}
