package com.mehdok.fineepublib.interfaces;

import android.net.Uri;
import android.webkit.WebView;

/**
 * Created by mehdok on 6/15/2016.
 */

public interface EpubTapListener {

    /**
     * user taped on email link
     * @param email address of email
     */
    void onEmailTapped(String email);

    /**
     * user taped on web url
     * @param link address of url link
     */
    void onWebTapped(String link);

    /**
     * user taped on empty space, do something or not
     */
    void onEmptySpaceTapped();

    /**
     * user tapped on any external link
     */
    void onExternalLinkCLicked(Uri uri);

    WebView.HitTestResult getHitTestResult();

    void onPageScrolled();
}
