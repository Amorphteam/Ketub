/*******************************************************************************
 * Programmer : Mehdi Sohrabi Email : mehdok@gmail.com
 ******************************************************************************/
package com.mehdok.fineepublib.epubviewer.XmlFilter;

import android.net.Uri;
import android.util.Log;

import com.mehdok.fineepublib.epubviewer.Globals;
import com.mehdok.fineepublib.epubviewer.IResourceSource;
import com.mehdok.fineepublib.epubviewer.XmlUtil;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

import java.io.IOException;

/*
 * Convert src element of <img> elements in XHTML to in-line DataURI
 */
public class InlineImageElementFilter extends XMLFilterImpl {
    private static final String IMG_ELEMENT_NAME = "img";

    private static final String ATTRIBUTE_SRC = "src";

    private IResourceSource mSource;
    private Uri mUri;

    /*
     * @param uri of the XML document being processed (used to resolve links)
     * @param source to fetch data from
     */
    public InlineImageElementFilter(Uri sourceUri, IResourceSource source) {
        mSource = source;
        mUri = sourceUri;
    }

    @Override
    public void startElement(String namespaceURI, String localName,
                             String qualifiedName, Attributes attrs) throws SAXException {
        if (localName.equals(IMG_ELEMENT_NAME)) {
            try {
                attrs = XmlUtil.replaceAttributeValueWithDataUri(mUri, mSource, attrs,
                        ATTRIBUTE_SRC);
            } catch (IOException e) {
                Log.e(Globals.TAG, "Error writing XML ", e);
            }
        }
        super.startElement(namespaceURI, localName, qualifiedName, attrs);
    }

}
