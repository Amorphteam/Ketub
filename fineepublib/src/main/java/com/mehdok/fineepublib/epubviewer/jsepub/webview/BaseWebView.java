package com.mehdok.fineepublib.epubviewer.jsepub.webview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ScrollView;

import com.mehdok.fineepublib.epubviewer.Globals;
import com.mehdok.fineepublib.epubviewer.HrefResolver;
import com.mehdok.fineepublib.epubviewer.ResourceResponse;
import com.mehdok.fineepublib.epubviewer.jsepub.JSBook;
import com.mehdok.fineepublib.epubviewer.jsepub.client.JsWebViewClient;
import com.mehdok.fineepublib.epubviewer.jsepub.client.WebViewClientListener;
import com.mehdok.fineepublib.epubviewer.util.BookAssetUtil;
import com.mehdok.fineepublib.interfaces.EpubScrollListener;
import com.mehdok.fineepublib.interfaces.EpubTapListener;

import java.io.IOException;

/**
 * Created by mehdok on 9/12/2016.
 */

public abstract class BaseWebView<T> extends WebView
        implements WebViewClientListener {
    protected JsWebViewClient jsWebViewClient;
    protected JSBook mBook;
    private EpubTapListener tapListener;
    private EpubScrollListener scrollListener;
    private EpubGestureDetector epubGestureDetector;
    private GestureDetector mGestureDetector;

    public BaseWebView(Context context) {
        super(context);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        init();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    protected void init() {
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        getSettings().setSupportZoom(false);
        getSettings().setBuiltInZoomControls(false);
        getSettings().setAllowFileAccess(true);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        jsWebViewClient = new JsWebViewClient(this);
        setWebViewClient(jsWebViewClient);
        setWebChromeClient(new WebChromeClient());

        setFadingEdgeLength(0);
        setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getSettings().setAllowUniversalAccessFromFileURLs(true);
            getSettings().setAllowFileAccessFromFileURLs(true);
        }

        epubGestureDetector = new EpubGestureDetector(tapListener);
        mGestureDetector = new GestureDetector(getContext(), epubGestureDetector);

        //        addTreeObserver();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event) ||
                super.onTouchEvent(event);
    }

    @Override
    public WebResourceResponse onRequest(String url) {
        // return local css
        if (url.contains(BookAssetUtil.STYLE_FOLIO)) {
            return BookAssetUtil.getInstance().getCssForName(getContext(), BookAssetUtil.STYLE_FOLIO);
        }

        // return local js
        if (url.contains(BookAssetUtil.JS_FOLIO)) {
            return BookAssetUtil.getInstance().getJsForName(getContext(), BookAssetUtil.JS_FOLIO);
        }

        url = correctUrlOpf(url);
        Uri resourceUri = Uri.parse(url);
        WebResourceResponse webResponse = new WebResourceResponse("", "UTF-8", null);
        ResourceResponse response = mBook.fetch(resourceUri);

        // if don't have resource, give error
        if (response == null) {
            Log.e("BaseWebView - onRequest", "Unable to find resource in epub");
        } else {
            webResponse.setData(response.getData());
            webResponse.setMimeType(response.getMimeType());
        }
        return webResponse;
    }

    @Override
    public void onPageFinished() {

    }

    @Override
    public void onPageStarted() {

    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return false;
    }

    public void onDestroy() {
        jsWebViewClient.setWebViewClientListener(null);
    }

    public void setBook(JSBook book) {
        mBook = book;
    }

    public void setBook(String fileName) throws IOException {
        // if book already loaded, don't load again
        if ((mBook == null) || !mBook.getFileName().equals(fileName)) {
            mBook = new JSBook(fileName);
        }
    }

    public abstract void loadResourcePage(T resource);

    protected String correctUrlOpf(String url) {
        HrefResolver resolver = new HrefResolver(mBook.getOpfFileName());
        String host = "http://localhost:" + Globals.WEB_SERVER_PORT;
        url = url.substring(host.length(), url.length());
        if (resolver.ToAbsolute("").equals("")) {
            url = host + url;
        } else {
            url = host + "/" + resolver.ToAbsolute("") + url;
        }

        return url;
    }

    public void exeJs(String js) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript(js, null);
        } else {
            loadUrl(js);
        }
    }

    public void setTapListener(EpubTapListener tapListener) {
        this.tapListener = tapListener;
        epubGestureDetector.setEpubTapListener(tapListener);
    }

    public void setScrollListener(EpubScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public int getLastSeenOffset(float percent) {
        return (int) (percent * getPageContentHeight());
    }

    public int getPageContentHeight() {
        return computeVerticalScrollRange();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollListener != null) {
            scrollListener.onPageScrolled(t);
        }
    }
}
