package com.mehdok.fineepublib.epubviewer;
/*******************************************************************************
 * Programmer : Mehdi Sohrabi Email : mehdok@gmail.com
 ******************************************************************************/

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;

import com.mehdok.fineepublib.epubviewer.epub.Book;
import com.mehdok.fineepublib.epubviewer.util.JQueryHandler;
import com.mehdok.fineepublib.interfaces.EpubListener;
import com.mehdok.fineepublib.interfaces.EpubProgressListener;
import com.mehdok.fineepublib.interfaces.EpubSwipeListener;
import com.mehdok.fineepublib.interfaces.EpubTapListener;
import com.mehdok.fineepublib.interfaces.JavascriptListener;

import java.io.IOException;
import java.util.ArrayList;

/*
 * Holds the logic for the App's
 * special WebView handling
 */
public abstract class EpubWebView extends WebView {

    public final static float FLING_THRESHOLD_VELOCITY = 200;
    public final static float FLING_THRESHOLD_VELOCITY_Y = 600;
    public static final int SCROLL_DOWN = 1;
    public static final int SCROLL_UP = -1;
    public static final String JQ_THEME_ORIGINAL = "javascript:jsThemeOriginal();";
    public static final String JQ_THEME_2 = "javascript:jsTheme_2();";
    public static final String JQ_THEME_3 = "javascript:jsTheme_3();";
    public static final String JQ_THEME_4 = "javascript:jsTheme_4();";
    private final static int NUM_PAGES_LOAD = 5;
    public final int FONT_INCREASE = 21;
    public final int FONT_DECREASE = 22;
    public final int FONT_NONE = 23;
    public final int LINE_SPACE_INCREASE = 31;
    public final int LINE_SPACE_DECREASE = 32;
    public final int LINE_SPACE_NONE = 33;
    public final int VISION_0 = 40;
    public final int VISION_1 = 41;
    public final int VISION_2 = 42;
    public final int VISION_3 = 43;
    public final int VISION_4 = 44;
    private final int TURN_LEFT = 7;
    private final int TURN_RIGHT = 8;
    private final int TURN_UP = 9;
    private final int TURN_DOWN = 10;
    private final int MAX_FONT_SIZE = 200;
    private final int MIN_FONT_SIZE = 70;
    //private Uri currUri;
    public Uri firstLoadedPageUri;
    public Uri lastLoadedPageUri;
    public Uri mCurrentResourceUri;
    public float mFlingMinDistance = 50;
    public float mYFlingMinDistance = 50;
    public boolean imageClicked = false;
    public boolean resetScale = false;
    public WebSettings settings;
    public String currLineSpace = "";
    public String currFontSize = "";
    public int fontChangeMode = FONT_NONE;
    public int lineSpaceChangeMode = LINE_SPACE_NONE;
    public int visionMode = VISION_0;
    protected boolean reloadFlag = true;
    protected EpubListener epubListener;
    int currentScroll = 0;
    int pageSize = 0;
    int pageDiff = 0;
    int offset = 0;
    String jsCallback2 =
            "javascript:(document.onreadystatechange = function() { if(document.readyState === \"complete\") { webviewScriptAPI.onLoad();}; })()";
    String jsDividerDown =
            "javascript:(function() { document.body.innerHTML += '<div id=\"beforeSep\" style=\"height:20px;\"></div> <div id=\"separator\" style=\"height:16px; position:absolute; left:0; right:0; background-color:#b1afaf;\"></div> <div id=\"afterSep\" style=\"height:20px;\"></div>';}())";
    String jsDividerUp =
            "javascript:(function() { document.body.innerHTML = '<div id=\"beforeSep\" style=\"height:20px;\"></div> <div id=\"separator\" style=\"height:16px; position:absolute; left:0; right:0; background-color:#b1afaf;\"></div> <div id=\"afterSep\" style=\"height:20px;\"></div>' + document.body.innerHTML;}())";
    boolean loadIntrupt = false;
    private int prePageSize = 0;
    private Book mBook;
    private GestureDetector mGestureDetector;
    private float mScrollY = 0.0f;
    private WebViewClient mWebViewClient;
    private int numberOfPage;
    //new var 2016 / 06 / 15
    private ScrollType scrollType = ScrollType.HORIZONTAL;
    private JavascriptListener javascriptListener;
    private EpubSwipeListener swipeListener;
    private EpubTapListener tapListener;
    protected JQueryHandler jQueryHandler;
    GestureDetector.SimpleOnGestureListener mGestureListener =
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX,
                                       float velocityY) {
                    float deltaX = event2.getX() - event1.getX();
                    float deltaY = event2.getY() - event1.getY();

                    if ((Math.abs(deltaX) < Math.abs(deltaY)) &&
                            (Math.abs(deltaY) > mYFlingMinDistance) &&
                            (Math.abs(velocityX) > FLING_THRESHOLD_VELOCITY_Y)) {
                        if (deltaY < 0) {
                            if (isNotNull(swipeListener))
                                return swipeListener.onSwipeUp();
                        } else {
                            if (isNotNull(swipeListener))
                                return swipeListener.onSwipeDown();
                        }
                    } else if ((Math.abs(deltaX) > Math.abs(deltaY)) &&
                            (Math.abs(deltaX) > mFlingMinDistance) &&
                            (Math.abs(velocityX) > FLING_THRESHOLD_VELOCITY)) {
                        if (deltaX < 0) {
                            if (isNotNull(swipeListener))
                                return swipeListener.onSwipeLeft(); //TODO check left
                        } else {
                            if (isNotNull(swipeListener))
                                return swipeListener.onSwipeRight();
                        }
                    }

                    return false;
                }

                public boolean onSingleTapConfirmed(MotionEvent e) {
                    HitTestResult hr = getHitTestResult();
                    String extra = hr.getExtra();
                    int type = hr.getType();

                    if (type == HitTestResult.EMAIL_TYPE) {
                        if (isNotNull(tapListener))
                            tapListener.onEmailTapped(extra);
                    } else if (type == HitTestResult.SRC_ANCHOR_TYPE) {
                        if (isNotNull(tapListener))
                            tapListener.onWebTapped(extra);
                    } else if (type == HitTestResult.UNKNOWN_TYPE) {
                        if (isNotNull(tapListener))
                            tapListener.onEmptySpaceTapped();
                    } else if (type == HitTestResult.IMAGE_TYPE) {
                        imageClicked = true;
                        settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
                        LoadUri(Uri.parse(extra));
                    } else {
                        LoadUri(Uri.parse(extra));
                    }

                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                        float distanceY) {
                    return false;
                }
            };
    private EpubProgressListener progressListener;

    public EpubWebView(Context context) {
        this(context, null);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("deprecation")
    public EpubWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, mGestureListener);
        settings = getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        addWebSettings(settings);
        setWebViewClient(mWebViewClient = createWebViewClient());
        setPictureListener(createPictureListener());
        setWebChromeClient(new WebChromeClient());

        jQueryHandler = new JQueryHandler(getContext());

        init();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowUniversalAccessFromFileURLs(true);
            settings.setAllowFileAccessFromFileURLs(true);
        }

        addJavascriptInterface(new ObjectExtension(), "webviewScriptAPI");
    }

    public void setEpubListener(EpubListener epubListener) {
        this.epubListener = epubListener;
    }

    public void removeEpubListener() {
        this.epubListener = null;
    }

    public void setProgressListener(EpubProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public void removeProgressListener() {
        this.progressListener = null;
    }

    protected boolean isNotNull(Object object) {
        return object != null;
    }

    public void setScrollType(ScrollType scrollType) {
        this.scrollType = scrollType;
    }

    public void setJavascriptListener(JavascriptListener javascriptListener) {
        this.javascriptListener = javascriptListener;
    }

    public void removeJavascriptListener() {
        this.javascriptListener = null;
    }

    public void setSwipeListener(EpubSwipeListener swipeListener) {
        this.swipeListener = swipeListener;
    }

    public void removeSwipeListener() {
        this.swipeListener = null;
    }

    public void setTapListener(EpubTapListener tapListener) {
        this.tapListener = tapListener;
    }

    public void removeTapListener() {
        this.tapListener = null;
    }

    abstract protected WebViewClient createWebViewClient();

    abstract protected PictureListener createPictureListener();

    abstract protected void addWebSettings(WebSettings settings);

    public Book getBook() {
        return mBook;
    }

    public void setBook(Book book) {
        mBook = book;
    }

    public void setBook(String fileName) throws IOException {
        // if book already loaded, don't load again
        if ((mBook == null) || !mBook.getFileName().equals(fileName)) {
            mBook = new Book(fileName);
        }
    }

    public WebViewClient getWebViewClient() {
        return mWebViewClient;
    }

    /*
     * Chapter of book to show
     */
    public void loadChapter(Uri resourceUri) {
        if (scrollType == ScrollType.VERTICAL) {
            if (mBook != null) {
                // if no chapter resourceName supplied, default to first one.
                if (resourceUri == null) {
                    resourceUri = mBook.nextResource(mBook.firstChapter());//TODO
                }
                if (resourceUri != null) {
                    mCurrentResourceUri = resourceUri;
                    firstLoadedPageUri = lastLoadedPageUri = mCurrentResourceUri;
                    prePageSize = 0;
                    clearCache(false);
                    LoadUri(resourceUri);
                    //nightModeCheck();
                }
            }
        } else if (scrollType == ScrollType.HORIZONTAL) {
            if (mBook != null) {
                // if no chapter resourceName supplied, default to first one.
                if (resourceUri == null) {
                    resourceUri = mBook.firstChapter();
                }
                if (resourceUri != null) {
                    mCurrentResourceUri = resourceUri;
                    clearCache(false);
                    LoadUri(resourceUri);
                }
            }
        }
    }

    protected abstract void LoadUri(Uri uri);

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event) ||
                super.onTouchEvent(event);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollType == ScrollType.VERTICAL) {
            currentScroll = this.getScrollY();
            pageSize = this.getContentHeight();

            pageDiff = pageSize - prePageSize;
            offset = 10 + prePageSize;

            if (reloadFlag && (currentScroll > offset) && ((t - oldt) > 0)) {
                prePageSize = pageSize;
                reloadFlag = false;
                // TODO: 1/7/2017 MEHDO inform the listener so it do operation in thread
                loadPageInTask(lastLoadedPageUri, SCROLL_DOWN);
            }
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (scrollType == ScrollType.VERTICAL) {
            if (reloadFlag && clampedY) {
                if (!(isVerticalScrollPossible(SCROLL_DOWN))) {
                    reloadFlag = false;
                    loadPageInTask(lastLoadedPageUri, SCROLL_DOWN);
                } else if (!(isVerticalScrollPossible(SCROLL_UP))) {
                    reloadFlag = false;
                    loadPageInTask(firstLoadedPageUri, SCROLL_UP);
                }
            }
        }
    }

    private boolean isVerticalScrollPossible(int direction) {
        final int offset = computeVerticalScrollOffset();
        final int range = computeVerticalScrollRange() - computeVerticalScrollExtent();
        if (range == 0) return false;
        if (direction < 0) {
            return offset > 0;
        } else {
            return offset < range - 1;
        }
    }

    public void onLoadCompleted() {
        reloadFlag = true;
        if (isNotNull(javascriptListener))
            javascriptListener.onJSLoadCompleted();
    }

    public void loadPageInTask(Uri pageUri, int scrollType) {
        loadIntrupt = false;
        getPageBodyTask(pageUri, scrollType);
    }

    //load body chunk by chunk
    public void loadBodyChunk(ArrayList<String> bodyChunks, int scrollType) {
        if (scrollType == SCROLL_DOWN) {
            //loadUrl(jsDividerDown);
            String jsResult2 = "";
            for (String body : bodyChunks) {
                if (mBook.nextResource(lastLoadedPageUri) != null) {
                    lastLoadedPageUri = mBook.nextResource(lastLoadedPageUri);
                }

                if (!loadIntrupt) {
                    exeJS(jsDividerDown);
                    jsResult2 = "javascript:jsAddToEndBody('" +
                            body +
                            "');"; // load on bottom
                    exeJS(jsResult2);
                }

            }
        } else if (scrollType == SCROLL_UP) {
            String jsResult = "";
            for (String body : bodyChunks) {
                if (mBook.previousResource(firstLoadedPageUri) != null) {
                    firstLoadedPageUri = mBook.previousResource(firstLoadedPageUri);
                }

                if (!loadIntrupt) {
                    exeJS(jsDividerUp);
                    jsResult = "javascript:jsAddToStartBody('" +
                            body +
                            "');";
                    exeJS(jsResult);
                }
            }
        }

        setBackgroundColor();
    }

    public void setLoadIntrupt() {
        loadIntrupt = true;
    }

    public void stopLoadingIcon() {
        if (isNotNull(progressListener))
            progressListener.onProgressStop();
    }

    public void startLoadingIcon() {
        if (isNotNull(progressListener))
            progressListener.onProgressStart();
    }

    public void increaseLineSpace(int i) {
        lineSpaceChangeMode = LINE_SPACE_INCREASE;

        if (i == 0) {
            post(new Runnable() {
                @Override
                public void run() {
                    String jsgetDefaultFont =
                            "javascript:jsChangeLineSpace('" +
                                    currLineSpace +
                                    "');";
                    exeJS(jsgetDefaultFont);
                }
            });
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    String jsgetDefaultFont =
                            "javascript:jsIncreaseLineSpace();";
                    exeJS(jsgetDefaultFont);
                }
            });
        }
    }

    public void decreaseLineSpace(int i) {
        lineSpaceChangeMode = LINE_SPACE_DECREASE;

        if (i == 0) {
            post(new Runnable() {
                @Override
                public void run() {
                    String jsgetDefaultFont =
                            "javascript:jsChangeLineSpace('" +
                                    currLineSpace +
                                    "');";
                    exeJS(jsgetDefaultFont);
                }
            });
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    String jsgetDefaultFont =
                            "javascript:jsDecreaseLineSpace();";
                    exeJS(jsgetDefaultFont);
                }
            });
        }
    }

    public void increaseFontSize(int i) {
        fontChangeMode = FONT_INCREASE;

        if (i == 0) {
            post(new Runnable() {
                @Override
                public void run() {
                    settings.setTextZoom(Integer.parseInt(currFontSize));
                }
            });
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    if (settings.getTextZoom() + 10 < MAX_FONT_SIZE) {
                        settings.setTextZoom(settings.getTextZoom() + 10);
                        currFontSize = settings.getTextZoom() + "";
                    }
                }
            });
        }
    }

    public void decreaseFontSize(final int i) {
        fontChangeMode = FONT_DECREASE;

        if (i == 0) {
            post(new Runnable() {
                @Override
                public void run() {
                    settings.setTextZoom(Integer.parseInt(currFontSize));
                }
            });
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    if (settings.getTextZoom() - 10 > MIN_FONT_SIZE) {
                        settings.setTextZoom(settings.getTextZoom() - 10);
                        currFontSize = settings.getTextZoom() + "";
                    }
                }
            });
        }
    }

    @TargetApi(19)
    public void exeJS(final String js) {
        this.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= 19) {
                    evaluateJavascript(js, null);
                } else {
                    loadUrl(js);
                }
            }
        });
    }

    public void setBackgroundColor() {
        if (visionMode == VISION_2) {
            exeJS(JQ_THEME_2);
        } else if (visionMode == VISION_3) {
            exeJS(JQ_THEME_3);
        } else if (visionMode == VISION_4) {
            exeJS(JQ_THEME_4);
        }
    }

    private Bitmap getShot() {
        Bitmap returnedBitmap = null;
        this.setDrawingCacheEnabled(true);
        returnedBitmap = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);

        return returnedBitmap;
    }

    @Override
    protected float getTopFadingEdgeStrength() {
        return 0.0f;
    }

    @Override
    protected float getBottomFadingEdgeStrength() {
        return 0.0f;
    }

    @Override
    protected float getLeftFadingEdgeStrength() {
        return 0.0f;
    }

    @Override
    protected float getRightFadingEdgeStrength() {
        return 0.0f;
    }

    private void init() {
        setFadingEdgeLength(0);
        setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
    }

    // TODO: 1/7/2017 MEHDOK do it in task like before
    public void getPageBodyTask(Uri currAddress, final int scroll) {
        if (isNotNull(progressListener))
            progressListener.onProgressStart();

        ArrayList<String> bodys = mBook.getNextPageBody(currAddress, scroll, NUM_PAGES_LOAD);
        if (bodys != null) {
            if (scroll == SCROLL_DOWN) {
                if (bodys.size() == 0) {
                    if (isNotNull(epubListener))
                        epubListener.onPageEnd();
                } else {
                    loadBodyChunk(bodys, scroll);
                }
            } else if (scroll == SCROLL_UP) {
                if (bodys.size() == 0) {
                    if (isNotNull(epubListener))
                        epubListener.onPageStart();
                } else {
                    loadBodyChunk(bodys, scroll);
                }
            }

            loadUrl(jsCallback2);

            if (isNotNull(epubListener))
                epubListener.onPageUpdated(lastLoadedPageUri);
        } else {
            if (isNotNull(progressListener))
                progressListener.onProgressStop();
        }
    }

    public enum ScrollType {
        VERTICAL,
        HORIZONTAL
    }

    public class ObjectExtension {
        @JavascriptInterface
        public void onLoad() {
            reloadFlag = true;

            if (isNotNull(javascriptListener))
                javascriptListener.onJSLoad();
        }

        @JavascriptInterface
        public void loading(String param) {
        }

        @JavascriptInterface
        public void getModifiedText(final String m, final String sw) {

        }

        @JavascriptInterface
        public void printString(String s) {
        }

        @JavascriptInterface
        public void searchMe(String s, String sw) {
            //TODO do diactitic removal on input
            System.out.println(s);
            String js = "";
            String replacement = " <div style=\"background-color:#f0da1e\">" + sw + "</div> ";
            int replaceLength = replacement.length();
            int swLength = sw.length();
            int preIndex = 0;
            String ss = "";
            ArrayList<Integer> result = new ArrayList<Integer>();
            int searchIndex = s.indexOf(sw, 0);
            while (searchIndex >= 0) {
                result.add(searchIndex);
                ss += s.substring(preIndex, searchIndex) + replacement;
                preIndex = searchIndex + swLength;
                searchIndex = s.indexOf(sw, searchIndex + 1);
            }
            System.out.println(ss);
            js = "javascript:(function(){document.documentElement.innerHTML =" + ss + ";})()";
            loadUrl(js);
            for (int r : result)
                System.out.println(r);
        }

        @JavascriptInterface
        public void informStartPage() {
            if (isNotNull(javascriptListener))
                javascriptListener.onJSStartPage();
        }

        @JavascriptInterface
        public void informEndPage() {
            if (isNotNull(javascriptListener))
                javascriptListener.onJSEndPage();
        }

        @JavascriptInterface
        public void getNumberOfPage(int i) {
            //TODO i number of pages - 1
            numberOfPage = i + 1;
            if (isNotNull(javascriptListener))
                javascriptListener.onJSGetPageNumber(i);

        }

        @JavascriptInterface
        public void updateCurrentPageNumber(int i) {
            if (isNotNull(javascriptListener))
                javascriptListener.onJSUpdateCurrentPageNumber(i, numberOfPage);
        }

        @JavascriptInterface
        public void setDefaultFont(String size) {
            currFontSize = size;
        }

        @JavascriptInterface
        public void setDefaultLineScape(String lineSpace) {
            currLineSpace = lineSpace;
        }
    }

    public void setCurrentPage(int i)
    {
        String jsSetPage = "javascript:setPage(" + i + ")";
        exeJS(jsSetPage);
    }

    public boolean changeChapter(Uri resourceUri) {
        if (resourceUri != null) {
            loadChapter(resourceUri);
            return true;
        } else {
            if (epubListener != null)
                epubListener.onPageEnd();
            return false;
        }
    }

    public void applyFontLineSpaceChange() {
        if (fontChangeMode == FONT_INCREASE) {
            increaseFontSize(0);
        } else if (fontChangeMode == FONT_DECREASE) {
            decreaseFontSize(0);
        }

        if (lineSpaceChangeMode == LINE_SPACE_INCREASE) {
            increaseLineSpace(0);
        } else if (lineSpaceChangeMode == LINE_SPACE_DECREASE) {
            decreaseLineSpace(0);
        }
    }
}
