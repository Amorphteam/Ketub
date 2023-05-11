package com.mehdok.fineepublib.epubviewer.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Base64;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mehdok on 6/28/2016.
 */

public class LocalAssetManager {
    private AssetManager assetManager;

    public LocalAssetManager(Context ctx)
    {
        assetManager = ctx.getAssets();
    }

    public String openAssetAsString(String address, boolean base64)
    {
        try
        {
            InputStream is = assetManager.open(address);
            int size;
            size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String str;

            if (base64)
                str = Base64.encodeToString(buffer, Base64.NO_WRAP);
            else
                str = new String(buffer);

            return str;

        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "";
        }
    }
}
