package com.mehdok.fineepublib.epubviewer.jsepub.webview;

import android.net.Uri;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebView;

import com.mehdok.fineepublib.interfaces.EpubTapListener;

/**
 * Created by mehdok on 9/18/2016.
 */

public class EpubGestureDetector extends GestureDetector.SimpleOnGestureListener {
    private EpubTapListener epubTapListener;

    public EpubGestureDetector(EpubTapListener epubTapListener) {
        this.epubTapListener = epubTapListener;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (epubTapListener != null) {
            epubTapListener.onPageScrolled();
        }
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        WebView.HitTestResult hr = epubTapListener.getHitTestResult();
        if (hr == null) return false;

        String extra = hr.getExtra();
        int type = hr.getType();

        if (type == WebView.HitTestResult.EMAIL_TYPE) {
            if (epubTapListener != null) {
                epubTapListener.onEmailTapped(extra);
            }
        } else if (type == WebView.HitTestResult.SRC_ANCHOR_TYPE) {
            if (epubTapListener != null) {
                epubTapListener.onWebTapped(extra);
            }
        } else if (type == WebView.HitTestResult.UNKNOWN_TYPE) {
            if (epubTapListener != null) {
                epubTapListener.onEmptySpaceTapped();
            }
        } else if (type == WebView.HitTestResult.IMAGE_TYPE) {
            // no op yet
        } else {
            if (epubTapListener != null) {
                epubTapListener.onExternalLinkCLicked(Uri.parse(extra));
            }
        }

        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (epubTapListener != null)
            epubTapListener.onDoubleTap();
        return super.onDoubleTap(e);
    }

    public void setEpubTapListener(EpubTapListener epubTapListener) {
        this.epubTapListener = epubTapListener;
    }
}
