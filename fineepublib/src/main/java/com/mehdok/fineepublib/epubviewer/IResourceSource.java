/*******************************************************************************
 * Programmer : Mehdi Sohrabi Email : mehdok@gmail.com
 ******************************************************************************/
package com.mehdok.fineepublib.epubviewer;

import android.net.Uri;

public interface IResourceSource {
    /*
     * Fetch the requested resource
     */
    public ResourceResponse fetch(Uri resourceUri);
}
