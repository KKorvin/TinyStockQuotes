package org.linuxspace.stockquotes.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.linuxspace.stockquotes.model.News;
import org.linuxspace.stockquotes.model.interfaces.INewsGetterCallback;
import org.linuxspace.stockquotes.utils.JsonConstants;

import java.util.ArrayList;

/**
 * Created by Alon on 15.01.2015.
 */
public class NewsGetter extends BasicAsyncTask {

    private static final String YAHOO_STOCK_NEWS_PIPE = "http://pipes.yahoo.com/pipes/pipe.run?_id=2FV68p9G3BGVbc7IdLq02Q&_render=json&feedcount=10&feedurl=http://finance.yahoo.com/rss/headline?s=";
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
            String url = buildJsonNewsUrl();
            JSONObject jsonValue = getJsonWithUrl(url).getJSONObject(JsonConstants.J_VALUE);
            JSONObject jsonItem = jsonValue.optJSONObject(JsonConstants.J_ITEMS);
            JSONArray jsonItems = new JSONArray();
            if (jsonItem == null) {
                jsonItems = jsonValue.getJSONArray(JsonConstants.J_ITEMS);
            } else {
                jsonItems.put(jsonItem);
            }
            newsItems.addAll(News.fromJson(jsonItems));
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
        return YAHOO_STOCK_NEWS_PIPE + stocksSymbol;
    }
}
