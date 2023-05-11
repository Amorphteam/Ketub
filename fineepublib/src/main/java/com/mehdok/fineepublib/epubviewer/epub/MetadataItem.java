/*******************************************************************************
 * Programmer : Mehdi Sohrabi Email : mehdok@gmail.com
 ******************************************************************************/
package com.mehdok.fineepublib.epubviewer.epub;

import org.xml.sax.Attributes;

public class MetadataItem {
    private static final String XML_ATTRIBUTE_NAME = "name";
    private static final String XML_ATTRIBUTE_CONTENT = "content";

    private String mName;
    private String mContent;

    //constructor
    public MetadataItem(Attributes attributes) {
        mName = attributes.getValue(XML_ATTRIBUTE_NAME);
        mContent = attributes.getValue(XML_ATTRIBUTE_CONTENT);
    }

    public String getName() {
        return mName;
    }

    public String getContent() {
        return mContent;
    }
}
