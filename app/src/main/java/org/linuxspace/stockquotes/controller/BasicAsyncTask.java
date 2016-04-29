package org.linuxspace.stockquotes.controller;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.linuxspace.stockquotes.utils.GlobalUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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

    protected Document getXmlWithUrl(String url) throws IOException, ParserConfigurationException, SAXException {
        URL link = new URL(url);
        HttpURLConnection mURLConnection = (HttpURLConnection) link.openConnection();
        HttpURLConnection.setFollowRedirects(true);
        mURLConnection.connect();
        InputStream rssStream = mURLConnection.getInputStream();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new InputSource(rssStream));
        doc.getDocumentElement().normalize();

        mURLConnection.disconnect();
        rssStream.close();

        return doc;
    }
}
