package com.mehdok.fineepublib.epubviewer.jsepub.webview;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;

import org.jsoup.nodes.Document;

/**
 * @author mehdok on 9/12/2016.
 *         <p>
 *         If the epub page is too large, This class will use to load the page step by step,
 *         all of the page string will sent to this class by he decise how much of it must be loaded right now
 *         by scrolling it adds more and more contents.
 */

public class SpliterWebView extends BaseWebView<Document> {
    private static final int CSS_CHAR = 0;//7328;
    private static final int JS_CHAR = 0;//8374;
    public static final int MAX_CHAR = CSS_CHAR + JS_CHAR + 15000;
    private Document resourceDoc = null;
    private int lastLoadedChunk = -1;

    public SpliterWebView(Context context) {
        super(context);
    }

    public SpliterWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpliterWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SpliterWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SpliterWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
    }

    @Override
    public void loadResourcePage(Document resource) {
        resourceDoc = resource;
    }
}
