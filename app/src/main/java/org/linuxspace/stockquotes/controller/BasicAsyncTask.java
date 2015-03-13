package org.linuxspace.stockquotes.controller;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.linuxspace.stockquotes.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Alon on 15.01.2015.
 */
public abstract class BasicAsyncTask extends AsyncTask<Void, Void, Void> {

    protected boolean wasError;

    /**
     * Gets json response from remote server by url
     */
    protected JSONObject getJsonWithUrl(String url) throws IOException, JSONException {

        URL link = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) link.openConnection();
        connection.setReadTimeout(Constants.INPUT_STREAM_READ_TIME_OUT);
        connection.setConnectTimeout(Constants.URL_CONNECTION_TIME_OUT);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-length", "0");
        connection.setUseCaches(false);
        connection.setAllowUserInteraction(false);
        connection.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        connection.disconnect();
        String result = sb.toString();
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject;
    }
}
