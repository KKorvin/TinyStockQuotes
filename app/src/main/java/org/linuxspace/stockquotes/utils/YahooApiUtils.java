package org.linuxspace.stockquotes.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * All functions which will help work with yahoo finance stored here
 */
public class YahooApiUtils {



    /**
     * Creates normal url from YQL query
     *
     * @return url
     */
    public static String createUrlFromQury(String query) throws MalformedURLException, URISyntaxException {
        String str = Constants.BASIC_YAHOO_API_URL + "format=json&q=" + query + "&env=" + Constants.YAHOO_DB_URL;
        URL url = new URL(str);
        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        return uri.toASCIIString();
    }
}
