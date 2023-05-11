package com.mehdok.fineepublib.interfaces;

/**
 * Created by mehdok on 6/15/2016.
 */

public interface JavascriptListener {
    // all operation must done in ui thread explicitly

    /**
     * javascript loaded successfully
     */
    void onJSLoad();

    /**
     * javascript informs that reached start of book
     */
    void onJSStartPage();

    /**
     * javascript informs that reached end of book
     */
    void onJSEndPage();

    /**
     * javascript response to getting current page number
     * @param i current page number
     */
    void onJSGetPageNumber(int i);

    /**
     * current page number updated
     * @param i
     * @param numberOfPage
     */
    void onJSUpdateCurrentPageNumber(int i, int numberOfPage);

    /**
     * on javascript load  completed
     */
    void onJSLoadCompleted();
}
