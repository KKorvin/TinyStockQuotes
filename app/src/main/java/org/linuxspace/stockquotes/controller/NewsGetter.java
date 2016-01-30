package org.linuxspace.stockquotes.controller;

import org.linuxspace.stockquotes.model.News;
import org.linuxspace.stockquotes.model.interfaces.INewsGetterCallback;
import org.w3c.dom.Document;

import java.util.ArrayList;

/**
 * Created by Alon on 15.01.2015.
 */
public class NewsGetter extends BasicAsyncTask {

    private static final String YAHOO_STOCK_NEWS_PART1 = "http://feeds.finance.yahoo.com/rss/2.0/headline?s=";
    private static final String YAHOO_STOCK_NEWS_PART2 = "&region=US&lang=en-US";
    private INewsGetterCallback callback;
    private String stocksSymbol;
    private ArrayList<News> newsItems;

    public NewsGetter(INewsGetterCallback callback, String stocksSymbol) {
        this.callback = callback;
        this.stocksSymbol = stocksSymbol;
        this.wasError = false;
        this.newsItems = new ArrayList<News>();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Document newsRss = getXmlWithUrl(buildJsonNewsUrl());
            newsItems.addAll(News.fromXML(newsRss));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        callback.onQuotesReceived(newsItems);
        super.onPostExecute(aVoid);
    }

    private String buildJsonNewsUrl() {
        return YAHOO_STOCK_NEWS_PART1 + stocksSymbol + YAHOO_STOCK_NEWS_PART2;
    }
}
