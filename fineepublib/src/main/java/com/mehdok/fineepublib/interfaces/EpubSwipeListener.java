package com.mehdok.fineepublib.interfaces;

/**
 * Created by mehdok on 6/15/2016.
 */

public interface EpubSwipeListener {

    /**
     * Called when user swiped left
     * @return whether handle it or not
     */
    boolean onSwipeLeft();

    /**
     * Called when user swiped right
     * @return whether handle it or not
     */
    boolean onSwipeRight();

    /**
     * Called when user swiped up
     * @return whether handle it or not
     */
    boolean onSwipeUp();

    /**
     * Called when user swiped down
     * @return whether handle it or not
     */
    boolean onSwipeDown();
}
