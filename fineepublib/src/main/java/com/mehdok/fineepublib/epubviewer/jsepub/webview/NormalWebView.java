package com.mehdok.fineepublib.epubviewer.jsepub.webview;

import android.content.Context;
import android.util.AttributeSet;

import com.mehdok.fineepublib.epubviewer.Globals;
import com.mehdok.fineepublib.epubviewer.HrefResolver;

/**
 * @author mehdok on 9/12/2016.
 *         <p>
 *         This class is responsible for loading pages with normal size and no difficulty
 */

public class NormalWebView extends BaseWebView<String> {
    public NormalWebView(Context context) {
        super(context);
    }

    public NormalWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NormalWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NormalWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public NormalWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
    }

    @Override
    public void loadResourcePage(String resource) {
        HrefResolver resolver = new HrefResolver(mBook.getOpfFileName());
        String basePath = "http://localhost:" + Globals.WEB_SERVER_PORT + "/" + resolver.ToAbsolute("");
        loadDataWithBaseURL(basePath, resource, "text/html", "UTF-8", null);
    }
}
