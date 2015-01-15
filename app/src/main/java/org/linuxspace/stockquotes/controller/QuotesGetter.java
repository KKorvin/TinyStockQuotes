package org.linuxspace.stockquotes.controller;

import org.json.JSONObject;
import org.linuxspace.stockquotes.model.interfaces.IQuotesGetterCallback;
import org.linuxspace.stockquotes.utils.YahooApiUtils;

/**
 * Created by Alon on 15.01.2015.
 */
public class QuotesGetter extends BasicAsyncTask {

    private IQuotesGetterCallback callback;
    private String stocksSymbols;
    private String result;

    public QuotesGetter(IQuotesGetterCallback callback, String stocksSymbols) {
        this.callback = callback;
        this.stocksSymbols = stocksSymbols;
        this.wasError = false;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            String query = buildQuotesGetQuery();
            String url = YahooApiUtils.createUrlFromQury(query);
            JSONObject jsonQuotes = getJsonWithUrl(url);
            result = jsonQuotes.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        callback.onQuotesReceived(result);
        super.onPostExecute(aVoid);
    }

    private String buildQuotesGetQuery() {
        String query = "select * from yahoo.finance.quoteslist where symbol in ('" + stocksSymbols + "')";
        return query;
    }
}
