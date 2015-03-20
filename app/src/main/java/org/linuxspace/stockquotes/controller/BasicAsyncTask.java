package org.linuxspace.stockquotes.controller;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.linuxspace.stockquotes.utils.GlobalUtils;

import java.io.IOException;

/**
 * Created by Alon on 15.01.2015.
 */
public abstract class BasicAsyncTask extends AsyncTask<Void, Void, Void> {

    protected boolean wasError;

    /**
     * Gets json response from remote server by url
     */
    protected JSONObject getJsonWithUrl(String url) throws IOException, JSONException {
        return GlobalUtils.getJsonWithUrl(url);
    }
}
