package org.linuxspace.stockquotes.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.linuxspace.stockquotes.utils.JsonConstants;

import java.util.ArrayList;

/**
 * Created by Alon on 15.01.2015.
 */
public class Stock extends FinanceItem {

    private static final String UNKNOWN = "N/A";

    public String name;
    public String symbol;
    public String volume;
    public String open;
    public String hight;
    public String low;
    public String prevClose;
    public String eps;
    public String lastTrade;

    /**
     * Create Stock from json object
     */
    public Stock(JSONObject jsonQuote) throws JSONException {
        this.name = jsonQuote.has(JsonConstants.J_NAME) ? jsonQuote.getString(JsonConstants.J_NAME) : "";
        this.symbol = jsonQuote.has(JsonConstants.J_SYMBOL_SMALL) ? jsonQuote.getString(JsonConstants.J_SYMBOL_SMALL) : "";
        if (this.symbol.isEmpty()) {
            this.symbol = jsonQuote.has(JsonConstants.J_SYMBOL_BIG) ? jsonQuote.getString(JsonConstants.J_SYMBOL_BIG) : "";
        }
        this.price = jsonQuote.has(JsonConstants.J_LAST_TRADE_PRICE) ? jsonQuote.getString(JsonConstants.J_LAST_TRADE_PRICE) : UNKNOWN;
        this.lastUpdate = jsonQuote.has(JsonConstants.J_LAST_TRADE_TIME) ? jsonQuote.getString(JsonConstants.J_LAST_TRADE_TIME) : "";
        this.priceChangeNumber = jsonQuote.has(JsonConstants.J_CHANGE_NUMBER) ? jsonQuote.getString(JsonConstants.J_CHANGE_NUMBER) : "";
        this.priceChangePercent = jsonQuote.has(JsonConstants.J_CHANGE_PERCENT) ? jsonQuote.getString(JsonConstants.J_CHANGE_PERCENT) : "";
        this.stockExchange = jsonQuote.has(JsonConstants.J_STOCK_EXCHANGE) ? jsonQuote.getString(JsonConstants.J_STOCK_EXCHANGE) : "";
        this.volume = jsonQuote.has(JsonConstants.J_VOLUME) ? jsonQuote.getString(JsonConstants.J_VOLUME) : "";
        this.open = jsonQuote.has(JsonConstants.J_OPEN) ? jsonQuote.getString(JsonConstants.J_OPEN) : "";
        this.prevClose = jsonQuote.has(JsonConstants.J_PREV_CLOSE) ? jsonQuote.getString(JsonConstants.J_PREV_CLOSE) : "";
        this.hight = jsonQuote.has(JsonConstants.J_HIGTH) ? jsonQuote.getString(JsonConstants.J_HIGTH) : "";
        this.low = jsonQuote.has(JsonConstants.J_LOW) ? jsonQuote.getString(JsonConstants.J_LOW) : "";
        this.eps = jsonQuote.has(JsonConstants.J_EPS) ? jsonQuote.getString(JsonConstants.J_EPS) : "";
        this.lastTrade = jsonQuote.has(JsonConstants.J_LAST_TRADE_TIME) ? jsonQuote.getString(JsonConstants.J_LAST_TRADE_TIME) : "";
        this.stockExchange = jsonQuote.has(JsonConstants.J_STOCK_EXCHANGE) ? jsonQuote.getString(JsonConstants.J_STOCK_EXCHANGE) : "";

        this.eps = this.eps.equals("null") || this.eps.equals("Null") ? UNKNOWN : this.eps;

    }

    /**
     * Factory method for creating array of stock from json array.
     */
    public static ArrayList<Stock> fromJson(JSONArray jsonQuotes) throws JSONException {
        ArrayList<Stock> stocks = new ArrayList<Stock>();
        for (int i = 0; i < jsonQuotes.length(); i++) {
            JSONObject jsonQuoteItem = (JSONObject) jsonQuotes.get(i);
            stocks.add(new Stock(jsonQuoteItem));
        }
        return stocks;
    }

    public String getBigLetter() {
        return this.name.substring(0, 1).toUpperCase();
    }

    @Override
    public int compareTo(FinanceItem item) {
        return this.symbol.compareTo(((Stock) item).symbol);
    }

}
