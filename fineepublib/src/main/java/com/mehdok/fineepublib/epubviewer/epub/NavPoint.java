/*******************************************************************************
 * Programmer : Mehdi Sohrabi Email : mehdok@gmail.com
 ******************************************************************************/
package com.mehdok.fineepublib.epubviewer.epub;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/*
 * Navpoint entry in a Table of Contents.
 */
public class NavPoint implements Parcelable {
    public static final Creator<NavPoint> CREATOR
            = new Creator<NavPoint>() {
        public NavPoint createFromParcel(Parcel in) {
            return new NavPoint(in);
        }

        public NavPoint[] newArray(int size) {
            return new NavPoint[size];
        }
    };
    private int mPlayOrder;
    private int depth;

    // parent id equals to playOrder - depth
    private int parentId;
    private String mNavLabel;
    private String mContent;

    /*
     * Construct as part of reading from XML
     */
    public NavPoint(String playOrder) {
        mPlayOrder = Integer.parseInt(playOrder);
    }

    public NavPoint(Parcel in) {
        mPlayOrder = in.readInt();
        mNavLabel = in.readString();
        mContent = in.readString();
    }

    public int getPlayOrder() {
        return mPlayOrder;
    }

    public void setPlayOrder(int playOrder) {
        mPlayOrder = playOrder;
    }

    public String getNavLabel() {
        return mNavLabel;
    }

    public void setNavLabel(String navLabel) {
        mNavLabel = navLabel;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    /*
             * Sometimes the content (resourceName) contains a tag
             * into the HTML.
             */
    public Uri getContentWithoutTag() {
        int indexOf = mContent.indexOf('#');
        String temp = mContent;
        if (0 < indexOf) {
            temp = mContent.substring(0, indexOf);
        }
        return Book.resourceName2Url(temp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mPlayOrder);
        dest.writeString(mNavLabel);
        dest.writeString(mContent);
    }

    public String getHashNavigation() {
        int indexOf = mContent.indexOf('#');
        String temp = null;
        if (0 < indexOf) {
            temp = mContent.substring(indexOf, mContent.length());
        }

        return temp;
    }

    @Override
    public String toString() {
        return "NavPoint{" +
                "mPlayOrder=" + mPlayOrder +
                ", depth=" + depth +
                ", parentId=" + parentId +
                ", mNavLabel='" + mNavLabel + '\'' +
                ", mContent='" + mContent + '\'' +
                '}';
    }
}
