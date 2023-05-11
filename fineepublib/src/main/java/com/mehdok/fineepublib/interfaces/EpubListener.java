package com.mehdok.fineepublib.interfaces;

import android.net.Uri;
import android.webkit.WebResourceResponse;

/**
 * Created by mehdok on 6/16/2016.
 */

public interface EpubListener {

    /**
     * Called when reaching the last page of epub
     */
    void onPageEnd();

    /**
     * Called when reaching the first page of epub
     */
    void onPageStart();

    /**
     * the page loaded
     * @param lastLoadedPageUri loaded uri page
     */
    void onPageUpdated(Uri lastLoadedPageUri);

    /**
     * The url requested, handle getting css, font, image ...
     * @param url requested url
     * @return {@link WebResourceResponse} of requested url
     */
    WebResourceResponse onUrlRequest(String url);
}
