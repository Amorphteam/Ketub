/*******************************************************************************
 * Programmer : Mehdi Sohrabi Email : mehdok@gmail.com
 ******************************************************************************/
package com.mehdok.fineepublib.epubviewer.epub;

import android.os.Parcel;
import android.os.Parcelable;

import com.mehdok.fineepublib.epubviewer.HrefResolver;

import org.xml.sax.Attributes;

/*
 * A row in the epub Manifest
 */
public class ManifestItem implements Parcelable {
    private static final String XML_ATTRIBUTE_ID = "id";
    private static final String XML_ATTRIBUTE_HREF = "href";
    private static final String XML_ATTRIBUTE_MEDIA_TYPE = "media-type";

    private String mHref;
    private String mID;
    private String mMediaType;

    /*
     * Construct from XML
     */
    public ManifestItem(Attributes attributes, HrefResolver resolver) {
        mHref = resolver.ToAbsolute(attributes.getValue(XML_ATTRIBUTE_HREF));
        mID = attributes.getValue(XML_ATTRIBUTE_ID);
        mMediaType = attributes.getValue(XML_ATTRIBUTE_MEDIA_TYPE);
    }

    public String getHref() {
        return mHref;
    }

    public String getID() {
        return mID;
    }

    public String getMediaType() {
        return mMediaType;
    }

    public static final Creator<ManifestItem> CREATOR
            = new Creator<ManifestItem>() {
        public ManifestItem createFromParcel(Parcel in) {
            return new ManifestItem(in);
        }

        public ManifestItem[] newArray(int size) {
            return new ManifestItem[size];
        }
    };

    public ManifestItem(Parcel in) {
        mHref = in.readString();
        mID = in.readString();
        mMediaType = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mHref);
        dest.writeString(mID);
        dest.writeString(mMediaType);
    }
}
