/*******************************************************************************
 * Programmer : Mehdi Sohrabi Email : mehdok@gmail.com
 ******************************************************************************/
package com.mehdok.fineepublib.epubviewer.epub;

import java.util.ArrayList;

public class Metadata {
    private static final String XML_ATTRIBUTE_COVER_IMAGE = "cover";
    private static final String XML_ATTRIBUTE_SIGILL_VERSION = "Sigil version";

    private ArrayList<MetadataItem> mItems;
    private String mTitle;
    private String mCreator;
    private String mPublisher;
    private String mLanguage;
    private String mIdentifier;

    public Metadata() {
        mItems = new ArrayList<MetadataItem>();
    }

    public void clear() {
        mItems.clear();
    }

    public void addItem(MetadataItem item) {
        mItems.add(item);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getCreator() {
        return mCreator;
    }

    public void setCreator(String creator) {
        mCreator = creator;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public void setPublisher(String publisher) {
        mPublisher = publisher;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

    public String getIdentifier() {
        return mIdentifier;
    }

    public void setIdentifier(String identifier) {
        mIdentifier = identifier;
    }

    public String getCoverImageAddress() {
        for (MetadataItem item : mItems) {
            if (item.getName() != null && item.getName().equalsIgnoreCase(XML_ATTRIBUTE_COVER_IMAGE)) {
                return item.getContent();
            }
        }

        return null;
    }

    public String getSigilVersion() {
        for (MetadataItem item : mItems) {
            if (item.getName() != null && item.getName().equalsIgnoreCase(XML_ATTRIBUTE_SIGILL_VERSION)) {
                return item.getContent();
            }
        }

        return null;
    }

}
