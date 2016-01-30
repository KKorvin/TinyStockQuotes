package org.linuxspace.stockquotes.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.linuxspace.stockquotes.model.FinanceItem;
import org.linuxspace.stockquotes.model.Stock;
import org.linuxspace.stockquotes.model.interfaces.IQuotesGetterCallback;
import org.linuxspace.stockquotes.utils.JsonXmlConstants;
import org.linuxspace.stockquotes.utils.YahooApiUtils;

import java.util.ArrayList;

/**
 * Created by Alon on 15.01.2015.
 */
public class QuotesGetter extends BasicAsyncTask {

    private IQuotesGetterCallback callback;
    private String[] stocksSymbols;
    private ArrayList<FinanceItem> financeItems;

    public QuotesGetter(IQuotesGetterCallback callback, String[] stocksSymbols) {
        this.callback = callback;
        this.stocksSymbols = stocksSymbols;
        this.wasError = false;
        this.financeItems = new ArrayList<FinanceItem>();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            String query = buildQuotesGetQuery();
            String url = YahooApiUtils.createUrlFromQury(query);
            JSONObject jsonQuery = getJsonWithUrl(url).getJSONObject(JsonXmlConstants.J_QUERY);
            JSONObject jsonResult = jsonQuery.getJSONObject(JsonXmlConstants.J_RESULTS);
            JSONObject jsonQuote = jsonResult.optJSONObject(JsonXmlConstants.J_QUOTE);
            JSONArray jsonQuotes = new JSONArray();
            if (jsonQuote == null) {
                jsonQuotes = jsonResult.getJSONArray(JsonXmlConstants.J_QUOTE);
            } else {
                jsonQuotes.put(jsonQuote);
            }
            financeItems.addAll(Stock.fromJson(jsonQuotes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        callback.onQuotesReceived(financeItems);
        super.onPostExecute(aVoid);
    }

    private String buildQuotesGetQuery() {
        String query = "select * from yahoo.finance.quotes where symbol in (";
        for (int i = 0; i < stocksSymbols.length; i++) {
            query += "'" + stocksSymbols[i] + "'";
            if (i != stocksSymbols.length - 1) {
                query += ",";
            }
        }
        query += ")";
        return query;
    }
}
