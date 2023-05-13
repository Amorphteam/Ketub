package com.mehdok.fineepublib.epubviewer.jsepub;

import android.content.Context;
import android.net.Uri;

import com.mehdok.fineepublib.epubviewer.epub.Book;
import com.mehdok.fineepublib.epubviewer.jsepub.util.SizeUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * @author mehdok on 9/11/2016.
 *         <p>
 *         This class is used for applying new style and javascript that used for new vertical scroll
 */

public class JSBook extends Book {

    private String adminCssPath = "../css/Style.css";
    private String adminJsPath = "../js/Bridge.js";
    private String additionalCssPath;
    private long bookSize = -1;

    public JSBook(String bookPath) throws IOException {
        super(bookPath);
    }

    public JSBook(String bookPath, String cssPath, String jsPath, String additionalCssPath) throws IOException {
        this(bookPath);
        adminCssPath = cssPath == null ? adminCssPath : cssPath;
        adminJsPath = jsPath == null ? adminJsPath : jsPath;
        this.additionalCssPath = additionalCssPath;
    }

    /**
     * @param ctx         The Context to access assets
     * @param resourceUri uri of resource to load
     * @param classes     default css classes, eg night mode or etc
     * @return An string containing all css and js plus the original html
     */
    public String getResourceString(Context ctx, Uri resourceUri, String classes) {
        if (resourceUri == null) {
            // TODO: 9/11/2016 return an empty head to prevent any not found exception
            return "";
        }

        InputStream in = fetchFromZip(url2ResourceName(resourceUri));
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String read = "";
        try {
            read = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (read != null) {
            sb.append(read);
            try {
                read = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return injectJsStyle(sb.toString(), classes);
    }

    /**
     * Getting the js script and css from assets and inject it to html head
     *
     * @param resourceString the html of requested resource
     * @param classes        default css classes, eg night mode or etc
     * @return An string containing all css and js plus the original html
     */
    private String injectJsStyle(String resourceString, String classes) {
        final String cssTag =
                String.format(Locale.getDefault(), "<link rel=\"stylesheet\" type=\"text/css\" href=\"%s\">",
                              adminCssPath);
        final String additionalCssTag = additionalCssPath == null ? "" :
                String.format(Locale.getDefault(), "<link rel=\"stylesheet\" type=\"text/css\" href=\"%s\">",
                              additionalCssPath);
        final String jsTag =
                String.format(Locale.getDefault(), "<script type=\"text/javascript\" src=\"%s\"></script>",
                              adminJsPath);

        final String toInject =
                String.format(Locale.getDefault(), "\n%s\n%s\n%s\n</head>", cssTag, jsTag, additionalCssTag);

        resourceString = resourceString.replace("</head>", toInject);
        resourceString = resourceString.replace("<html ",
                                                String.format(Locale.getDefault(), "<html class=\"%s\" ",
                                                              classes));

        return resourceString;
    }

    /**
     * @return size of all html file in book in unit of Byte
     */
    public long getBookSize() {
        if (bookSize < 0) {
            bookSize = SizeUtil.getAllBytes(this);
        }
        return bookSize;
    }

    /**
     * @param resourceAddress current chapter uri
     * @return size of resource before current file in book in unit of Byte
     */
    public float getChapterPercent(Uri resourceAddress) {
        return SizeUtil.getChapterBytes(resourceAddress, this) / (float) (getBookSize()) * 100;
    }

    public long getCurrentChapterSize(Uri resourceAddress) {
        return SizeUtil.getCurrentChapterBytes(resourceAddress, this);
    }

    public long getNextChapterSize(Uri resourceAddress) {
        Uri nextResource = nextResource(resourceAddress);
        if (nextResource != null) {
            return SizeUtil.getCurrentChapterBytes(nextResource, this);
        }
        return 1;
    }
}
