package org.linuxspace.stockquotes.controller;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Alon on 15.01.2015.
 */
public abstract class BasicAsyncTask extends AsyncTask<Void, Void, Void> {

    protected boolean wasError;

    protected JSONObject getJsonWithUrl(String url) throws IOException, JSONException {

        // HttpClient is more then less deprecated. Need to change to URLConnection
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();

        // Read content & Log
        InputStream inputStream = httpEntity.getContent();


        BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
        StringBuilder sBuilder = new StringBuilder();

        String line = null;
        while ((line = bReader.readLine()) != null) {
            sBuilder.append(line + "\n");
        }

        inputStream.close();
        String result = sBuilder.toString();
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject;
    }
}
