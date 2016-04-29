package org.linuxspace.stockquotes.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * All functions which will help work with yahoo finance stored here
 */
public class YahooApiUtils {

    //Yahoo urls
    public static final String BASIC_YAHOO_API_URL = "https://query.yahooapis.com/v1/public/yql?";
    public static final String YAHOO_DB_URL = "store://datatables.org/alltableswithkeys";

    /**
     * Creates normal url from YQL query
     *
     * @return url
     */
    public static String createUrlFromQury(String query) throws MalformedURLException, URISyntaxException {
        String str = BASIC_YAHOO_API_URL + "format=json&q=" + query + "&env=" + YAHOO_DB_URL;
        URL url = new URL(str);
        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        return uri.toASCIIString();
    }
}
