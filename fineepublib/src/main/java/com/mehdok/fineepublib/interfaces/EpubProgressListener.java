package com.mehdok.fineepublib.interfaces;

/**
 * Created by mehdok on 6/16/2016.
 */

public interface EpubProgressListener {

    /**
     * Called when start loading page
     */
    void onProgressStart();

    /**
     * Called when page loading finished
     */
    void onProgressStop();
}
