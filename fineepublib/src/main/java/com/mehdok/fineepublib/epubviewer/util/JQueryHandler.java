package com.mehdok.fineepublib.epubviewer.util;

import android.content.Context;
import android.util.LruCache;

/**
 * Created by mehdok on 6/28/2016.
 */

public class JQueryHandler {
    public static final String JQUERY_ASSET_ADDRESS = "js/jquery.min.js";
    public static final String SCRIPT_ASSET_ADDRESS = "js/script.js";
    private static JQueryHandler Instance;
    private LruCache<String, String> scriptLruCache;
    private LocalAssetManager localAssetManager;

    public static JQueryHandler getInstance(Context ctx)
    {
        if (Instance == null)
        {
            Instance = new JQueryHandler(ctx);
        }

        return Instance;
    }

    public JQueryHandler(Context ctx)
    {
        localAssetManager = new LocalAssetManager(ctx);
        scriptLruCache = new LruCache<>((int) (Runtime.getRuntime().maxMemory() / 1024 / 20));
    }

    /**
     * injecting a script to html head, it also include local jQuery
     *
     * @param html
     * @param scriptAddress
     * @return
     */
    public String injectScriptToHtml(String html, String scriptAddress)
    {
        if (scriptLruCache.get(scriptAddress) == null)
            scriptLruCache.put(scriptAddress, localAssetManager.openAssetAsString(scriptAddress, false));

        if (scriptLruCache.get(JQUERY_ASSET_ADDRESS) == null)
            scriptLruCache.put(JQUERY_ASSET_ADDRESS, localAssetManager.openAssetAsString(JQUERY_ASSET_ADDRESS, false));

        String header = "<head>" +
                "<script>" +
                scriptLruCache.get(JQUERY_ASSET_ADDRESS) +
                "</script>" +
                "<script>" +
                scriptLruCache.get(scriptAddress) +
                "</script>";

        html = html.replace("<head>", header);

        return html;
    }

    /**
     * Using a cache system to get jquery library
     *
     * @return String of jquery lib
     */
    public String getJQueryLib() {
        if (scriptLruCache.get(JQUERY_ASSET_ADDRESS) == null)
            scriptLruCache.put(JQUERY_ASSET_ADDRESS, localAssetManager.openAssetAsString(JQUERY_ASSET_ADDRESS, false));

        return scriptLruCache.get(JQUERY_ASSET_ADDRESS);
    }

    /**
     * Using a cache system to get script
     *
     * @return String of local jquery script
     */
    public String getLocalJqueryScript() {
        if (scriptLruCache.get(SCRIPT_ASSET_ADDRESS) == null)
            scriptLruCache.put(SCRIPT_ASSET_ADDRESS, localAssetManager.openAssetAsString(SCRIPT_ASSET_ADDRESS, false));

        return scriptLruCache.get(SCRIPT_ASSET_ADDRESS);
    }
}
