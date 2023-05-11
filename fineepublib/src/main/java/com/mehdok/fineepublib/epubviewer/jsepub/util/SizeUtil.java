package com.mehdok.fineepublib.epubviewer.jsepub.util;

import android.net.Uri;

import com.mehdok.fineepublib.epubviewer.epub.Book;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mehdok on 9/28/2016.
 */

public class SizeUtil {
    public static long getAllBytes(Book book) {
        long all = 0;
        Uri curr = book.firstChapter();
        all += getInputSize(curr, book);
        curr = book.nextResource(curr);

        while (curr != null) {
            all += getInputSize(curr, book);
            curr = book.nextResource(curr);
        }

        return all;
    }

    public static long getChapterBytes(Uri chapter, Book book) {
        long size = 0;
        Uri curr = book.firstChapter();
        while (!curr.getPath().equals(chapter.getPath())) {
            size += getInputSize(curr, book);
            curr = book.nextResource(curr);
        }
        return size;
    }

    public static long getCurrentChapterBytes(Uri chapter, Book book) {
        return getInputSize(chapter, book);
    }

    private static long getInputSize(Uri address, Book book) {
        return book.fetch(address).getSize();
    }

    private static byte[] getBytes(InputStream is) throws IOException {
        int len;
        int size = 1024;
        byte[] buf;

        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1) {
                bos.write(buf, 0, len);
            }
            buf = bos.toByteArray();
        }
        return buf;
    }
}
