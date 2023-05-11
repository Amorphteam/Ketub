/*******************************************************************************
 * Programmer : Mehdi Sohrabi Email : mehdok@gmail.com
 ******************************************************************************/
package com.mehdok.fineepublib.epubviewer.epub;

import android.net.Uri;
import android.sax.Element;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;

import com.mehdok.fineepublib.epubviewer.Globals;
import com.mehdok.fineepublib.epubviewer.HrefResolver;
import com.mehdok.fineepublib.epubviewer.IResourceSource;
import com.mehdok.fineepublib.epubviewer.ResourceResponse;
import com.mehdok.fineepublib.epubviewer.XmlUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/*
 * Represents a book that's been packed into an epub file
 */
public class Book implements IResourceSource {
    private final static String HTTP_SCHEME = "http";

    // the container XML
    private static final String XML_NAMESPACE_CONTAINER =
            "urn:oasis:names:tc:opendocument:xmlns:container";
    private static final String XML_ELEMENT_CONTAINER = "container";
    private static final String XML_ELEMENT_ROOTFILES = "rootfiles";
    private static final String XML_ELEMENT_ROOTFILE = "rootfile";
    private static final String XML_ATTRIBUTE_FULLPATH = "full-path";
    private static final String XML_ATTRIBUTE_MEDIATYPE = "media-type";

    // the .opf XML
    private static final String XML_NAMESPACE_PACKAGE = "http://www.idpf.org/2007/opf";
    private static final String XML_ELEMENT_PACKAGE = "package";
    private static final String XML_ELEMENT_MANIFEST = "manifest";
    private static final String XML_ELEMENT_MANIFESTITEM = "item";
    private static final String XML_ELEMENT_SPINE = "spine";
    private static final String XML_ATTRIBUTE_TOC = "toc";
    private static final String XML_ELEMENT_ITEMREF = "itemref";
    private static final String XML_ATTRIBUTE_IDREF = "idref";
    private static final String XML_ELEMENT_METADATA = "metadata";
    private static final String XML_METADATA_NAMESPACE = "http://purl.org/dc/elements/1.1/";
    private static final String XML_ELEMENT_TITLE = "title";
    private static final String XML_ELEMENT_PUBLISHER = "publisher";
    private static final String XML_ELEMENT_CREATOR = "creator";
    private static final String XML_ELEMENT_LANGUAGE = "language";
    private static final String XML_ELEMENT_IDENTIFIER = "identifier";
    private static final String XML_METADATA_META = "meta";

    /*
     * The zip archive
     */
    private ZipFile mZip;

    /*
     * Name of the ".opf" file in the zip archive
     */
    private String mOpfFileName;

    /*
     * Id of the "table of contents" entry in manifest
     */
    private String mTocID;
    private Metadata mMetadata;
    /*
     *  The resources that are in the spine element of the metadata.
     */
    private ArrayList<ManifestItem> mSpine;
    /*
     *  The manifest entry in the metadata.
     */
    private Manifest mManifest;
    /*
     *  The Table of Contents in the metadata.
     */
    private TableOfContents mTableOfContents;

    /*
     * Intended for unit testing
     */
    public Book() {
        mSpine = new ArrayList<ManifestItem>();
        mManifest = new Manifest();
        mTableOfContents = new TableOfContents();
        mMetadata = new Metadata();
    }

    /*
     * Constructor
     * @param fileName the filename of the Zip archive file
     */
    public Book(String fileName) throws IOException {
        mSpine = new ArrayList<ManifestItem>();
        mManifest = new Manifest();
        mTableOfContents = new TableOfContents();
        mMetadata = new Metadata();
        mZip = new ZipFile(fileName);
        parseEpub();
    }

    /*
     * @param url used by WebView
     * @return resourceName used by zip file
     */
    public static String url2ResourceName(Uri url) {
        // we only care about the path part of the URL
        String resourceName = url.getPath();

        // if path has a '/' prepended, strip it
        if (resourceName.charAt(0) == '/') {
            resourceName = resourceName.substring(1);
        }
        return resourceName;
    }

    /*
     * @param resourceName used by zip file
     * @return URL used by WebView
     */
    public static Uri resourceName2Url(String resourceName) {
        // build path assuming local file.
        // pack resourceName into path section of a file URI
        // need to leave '/' chars in path, so WebView is aware
        // of path to current resource, so it can can correctly resolve
        // path of any relative URLs in the current resource.
        return new Uri.Builder().scheme(HTTP_SCHEME)
                .encodedAuthority("localhost:" + Globals.WEB_SERVER_PORT)
                .appendEncodedPath(Uri.encode(resourceName, "/"))
                .build();
    }

    // Allow access to state for unit tests.
    public String getOpfFileName() {
        return mOpfFileName;
    }

    public String getTocID() {
        return mTocID;
    }

    public ArrayList<ManifestItem> getSpine() {
        return mSpine;
    }

    public Manifest getManifest() {
        return mManifest;
    }

    public TableOfContents getTableOfContents() {
        return mTableOfContents;
    }

    public Metadata getMetadata() {
        return mMetadata;
    }

    /*
     * Name of zip file
     */
    public String getFileName() {
        return (mZip == null) ? null : mZip.getName();
    }

    /*
     * Fetch file from zip
     */
    protected InputStream fetchFromZip(String fileName) {
        InputStream in = null;
        ZipEntry containerEntry = mZip.getEntry(fileName);
        if (containerEntry != null) {
            try {
                in = mZip.getInputStream(containerEntry);
            } catch (IOException e) {
                Log.e(Globals.TAG, "Error reading zip file " + fileName, e);
            }
        }

        if (in == null) {
            Log.e(Globals.TAG, "Unable to find file in zip: " + fileName);
        }

        return in;
    }

    /*
     * Fetch resource from ebook
     */
    public ResourceResponse fetch(Uri resourceUri) {
        String resourceName = url2ResourceName(resourceUri);
        ManifestItem item = mManifest.findByResourceName(resourceName);
        if (item != null) {
            ResourceResponse response = new ResourceResponse(item.getMediaType(),
                    fetchFromZip(resourceName));
            response.setSize(mZip.getEntry(resourceName).getSize());
            return response;
        }

        // if get here, something went wrong
        Log.e(Globals.TAG, "Unable to find resource in ebook " + resourceName);
        return null;
    }

    public Uri firstChapter() {
        return 0 < mSpine.size() ? resourceName2Url(mSpine.get(0).getHref()) : null;
    }

    /*
     * @return URI of next resource in sequence, or null if not one
     */
    public Uri nextResource(Uri resourceUri) {
        String resourceName = url2ResourceName(resourceUri);
        for (int i = 0; i < mSpine.size() - 1; ++i) {
            if (mSpine.get(i).getHref().equals(resourceName)) {
                return resourceName2Url(mSpine.get(i + 1).getHref());
            }
        }
        // if get here, not found
        return null;
    }

    /*
     * @return URI of previous resource in sequence, or null if not one
     */
    public Uri previousResource(Uri resourceUri) {
        String resourceName = url2ResourceName(resourceUri);
        for (int i = 1; i < mSpine.size(); ++i) {
            if (mSpine.get(i).getHref().equals(resourceName)) {
                return resourceName2Url(mSpine.get(i - 1).getHref());
            }
        }
        // if get here not found
        return null;
    }

    /*
     * Build up structure of epub
     */
    private void parseEpub() {
        // clear everything
        mOpfFileName = null;
        mTocID = null;
        mSpine.clear();
        mManifest.clear();
        mTableOfContents.clear();
        mMetadata.clear();

        // get the "container" file, this tells us where the ".opf" file is
        parseXmlResource("META-INF/container.xml", constructContainerFileParser());

        if (mOpfFileName != null) {
            parseXmlResource(mOpfFileName, constructOpfFileParser());
        }

        if (mTocID != null) {
            ManifestItem tocManifestItem = mManifest.findById(mTocID);
            if (tocManifestItem != null) {
                String tocFileName = tocManifestItem.getHref();
                HrefResolver resolver = new HrefResolver(tocFileName);
                parseXmlResource(tocFileName, mTableOfContents.constructTocFileParser(resolver));
            }
        }
    }

    private void parseXmlResource(String fileName, ContentHandler handler) {
        InputStream in = fetchFromZip(fileName);
        if (in != null) {
            XmlUtil.parseXmlResource(in, handler, null);
        }
    }

    /*
     * build parser to parse the container file,
     * i.e. get the name of the ".opf" file in the zip.
     * @return parser
     */
    public ContentHandler constructContainerFileParser() {
        // describe the relationship of the elements
        RootElement root = new RootElement(XML_NAMESPACE_CONTAINER, XML_ELEMENT_CONTAINER);
        Element rootfiles = root.getChild(XML_NAMESPACE_CONTAINER, XML_ELEMENT_ROOTFILES);
        Element rootfile = rootfiles.getChild(XML_NAMESPACE_CONTAINER, XML_ELEMENT_ROOTFILE);

        rootfile.setStartElementListener(new StartElementListener() {
            public void start(Attributes attributes) {
                String mediaType = attributes.getValue(XML_ATTRIBUTE_MEDIATYPE);
                if ((mediaType != null) && mediaType.equals("application/oebps-package+xml")) {
                    mOpfFileName = attributes.getValue(XML_ATTRIBUTE_FULLPATH);
                }
            }
        });
        return root.getContentHandler();
    }

    /*
     * build parser to parse the ".opf" file,
     * @return parser
     */
    public ContentHandler constructOpfFileParser() {
        // describe the relationship of the elements
        RootElement root = new RootElement(XML_NAMESPACE_PACKAGE, XML_ELEMENT_PACKAGE);
        Element manifest = root.getChild(XML_NAMESPACE_PACKAGE, XML_ELEMENT_MANIFEST);
        Element manifestItem = manifest.getChild(XML_NAMESPACE_PACKAGE, XML_ELEMENT_MANIFESTITEM);
        Element spine = root.getChild(XML_NAMESPACE_PACKAGE, XML_ELEMENT_SPINE);
        Element itemref = spine.getChild(XML_NAMESPACE_PACKAGE, XML_ELEMENT_ITEMREF);

        //matadata stuff
        Element metadata = root.getChild(XML_NAMESPACE_PACKAGE, XML_ELEMENT_METADATA);
        Element title = metadata.getChild(XML_METADATA_NAMESPACE, XML_ELEMENT_TITLE);
        Element creator = metadata.getChild(XML_METADATA_NAMESPACE, XML_ELEMENT_CREATOR);
        Element publisher = metadata.getChild(XML_METADATA_NAMESPACE, XML_ELEMENT_PUBLISHER);
        Element language = metadata.getChild(XML_METADATA_NAMESPACE, XML_ELEMENT_LANGUAGE);
        Element identifier = metadata.getChild(XML_METADATA_NAMESPACE, XML_ELEMENT_IDENTIFIER);
        Element meta = metadata.getChild(XML_NAMESPACE_PACKAGE, XML_METADATA_META);

        //parsing metadata

        title.setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String body) {
                mMetadata.setTitle(body);
            }
        });

        creator.setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String body) {
                mMetadata.setCreator(body);
            }
        });

        publisher.setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String body) {
                mMetadata.setPublisher(body);
            }
        });

        language.setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String body) {
                mMetadata.setLanguage(body);
            }
        });

        identifier.setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String body) {
                mMetadata.setIdentifier(body);
            }
        });

        meta.setStartElementListener(new StartElementListener() {
            @Override
            public void start(Attributes attributes) {
                mMetadata.addItem(new MetadataItem(attributes));
            }
        });

        final HrefResolver resolver = new HrefResolver(mOpfFileName);
        manifestItem.setStartElementListener(new StartElementListener() {
            public void start(Attributes attributes) {
                mManifest.add(new ManifestItem(attributes, resolver));
            }
        });

        // get name of Table of Contents file from the Spine
        spine.setStartElementListener(new StartElementListener() {
            public void start(Attributes attributes) {
                mTocID = attributes.getValue(XML_ATTRIBUTE_TOC);
            }
        });

        itemref.setStartElementListener(new StartElementListener() {
            public void start(Attributes attributes) {
                String temp = attributes.getValue(XML_ATTRIBUTE_IDREF);
                if (temp != null) {
                    ManifestItem item = mManifest.findById(temp);
                    if (item != null) {
                        mSpine.add(item);
                    }
                }
            }
        });

        return root.getContentHandler();
    }

    //get book meta
    public String getBookName() {
        if (mMetadata.getTitle() == null) {
            return "no_name";
        } else {
            return mMetadata.getTitle();
        }
    }

    public String getBookAuthor() {
        if (mMetadata.getCreator() == null) {
            return "no_author";
        } else {
            return mMetadata.getCreator().toLowerCase(Locale.US).trim();
        }
    }

    public String getBookPublisher() {
        if (mMetadata.getPublisher() == null) {
            return "no_publisher";
        } else {
            return mMetadata.getPublisher();
        }
    }

    public String getBookLanguage() {
        if (mMetadata.getLanguage() == null) {
            return "no_language";
        } else {
            return mMetadata.getLanguage();
        }
    }

    public String getBookIdentifier() {
        if (mMetadata.getIdentifier() == null) {
            return "no_identifier";
        } else {
            return mMetadata.getIdentifier();
        }
    }

    public String getSigilVersion() {
        if (mMetadata.getSigilVersion() == null) {
            return "no_sigilVersion";
        } else {
            return mMetadata.getSigilVersion();
        }
    }

    public Uri getCoverImage() {
        ManifestItem mi = mManifest.findById(mMetadata.getCoverImageAddress());
        if (mi == null) {
            return null;
        } else {
            return (resourceName2Url(mi.getHref()));
        }
    }

    public ArrayList<String> getNextPageBody(Uri currAddress, int scrollType, int howManyPages) {
        ArrayList<String> chunk = new ArrayList<String>();

        if (currAddress == null) {
            return chunk;
        }

        Uri nextAddress = currAddress;
        for (int i = 0; i < howManyPages; i++) {
            if (nextAddress != null) {
                nextAddress = nextResource(nextAddress);

                if (nextAddress != null) {
                    chunk.add(getPageBody(nextAddress));
                }
            }
        }

        return chunk;
    }

    public String getPageBody(Uri currAddress) {

        if (currAddress == null) {
            return "";
        }

        InputStream in = fetchFromZip(url2ResourceName(currAddress));

        //get html file as string
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
            //System.out.println(read);
            sb.append(read);
            try {
                read = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String html = sb.toString();

        Document doc = Jsoup.parse(html);

        Elements elements = doc.select("body").first().children();
        String body = elements.toString();

        // TODO: 9/11/2016
        //return getCleanString(body);
        return body;
    }

    private String getCleanString(String str) {
        str = str.replace("'", "\\'");
        str = str.replaceAll(System.getProperty("line.separator"), " ");
        str = str.replace("&laquo;", "«");
        str = str.replace("&raquo;", "»");

        return str;
    }

    public boolean isOnePage() {
        if (getSpine().size() == 1)
        {
            return true;
        } else {
            return false;
        }
    }

    public int getPageNumber() {
        return getSpine().size();
    }

    public String getNavTitle(String uri) {
        ArrayList<NavPoint> allPreviousNumbers = new ArrayList<>();

        for (NavPoint nav : getTableOfContents().getNavPoints()) {
            Integer secNumber = Integer.valueOf(nav.getContentWithoutTag().toString().split("Text/")[1].replace(".xhtml", ""));
            Integer uriNumber = Integer.valueOf(uri.split("Text/")[1].replace(".xhtml", ""));

            if (secNumber.equals(uriNumber) && !nav.getNavLabel().contains("الجزء")){
                return nav.getNavLabel();
            }else if (secNumber<uriNumber || (secNumber.equals(uriNumber) && nav.getNavLabel().contains("الجزء"))){
                // must get last section
                if (!nav.getNavLabel().contains("الجزء")){
                    allPreviousNumbers.add(nav);

                }

            }

        }

        return allPreviousNumbers.get(allPreviousNumbers.size()-1).getNavLabel();
    }

    private void logLong(String veryLongString) {
        int maxLogSize = 1000;
        for (int i = 0; i <= veryLongString.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > veryLongString.length() ? veryLongString.length() : end;
        }
    }

    public int getResourceNumber(Uri resource) {
        String resourceName = url2ResourceName(resource);
        for (int i = 0; i < getSpine().size(); i++) {
            if (resourceName.equals(getSpine().get(i).getHref())) {
                return i;
            }
        }

        return -1;
    }
}

