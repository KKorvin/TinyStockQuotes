package org.linuxspace.stockquotes.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.linuxspace.stockquotes.utils.Constants;

import java.util.ArrayList;

/**
 * Created by Alon on 15.01.2015.
 */
public class Stock extends FinanceItem {
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
        this.name = jsonQuote.has(Constants.J_NAME) ? jsonQuote.getString(Constants.J_NAME) : "";
        this.symbol = jsonQuote.has(Constants.J_SYMBOL_SMALL) ? jsonQuote.getString(Constants.J_SYMBOL_SMALL) : "";
        if (this.symbol.isEmpty()) {
            this.symbol = jsonQuote.has(Constants.J_SYMBOL_BIG) ? jsonQuote.getString(Constants.J_SYMBOL_BIG) : "";
        }
        this.price = jsonQuote.has(Constants.J_LAST_TRADE_PRICE) ? jsonQuote.getString(Constants.J_LAST_TRADE_PRICE) : Constants.UNKNOWN;
        this.lastUpdate = jsonQuote.has(Constants.J_LAST_TRADE_TIME) ? jsonQuote.getString(Constants.J_LAST_TRADE_TIME) : "";
        this.priceChangeNumber = jsonQuote.has(Constants.J_CHANGE_NUMBER) ? jsonQuote.getString(Constants.J_CHANGE_NUMBER) : "";
        this.priceChangePercent = jsonQuote.has(Constants.J_CHANGE_PERCENT) ? jsonQuote.getString(Constants.J_CHANGE_PERCENT) : "";
        this.stockExchange = jsonQuote.has(Constants.J_STOCK_EXCHANGE) ? jsonQuote.getString(Constants.J_STOCK_EXCHANGE) : "";
        this.volume = jsonQuote.has(Constants.J_VOLUME) ? jsonQuote.getString(Constants.J_VOLUME) : "";
        this.open = jsonQuote.has(Constants.J_OPEN) ? jsonQuote.getString(Constants.J_OPEN) : "";
        this.prevClose = jsonQuote.has(Constants.J_PREV_CLOSE) ? jsonQuote.getString(Constants.J_PREV_CLOSE) : "";
        this.hight = jsonQuote.has(Constants.J_HIGTH) ? jsonQuote.getString(Constants.J_HIGTH) : "";
        this.low = jsonQuote.has(Constants.J_LOW) ? jsonQuote.getString(Constants.J_LOW) : "";
        this.eps = jsonQuote.has(Constants.J_EPS) ? jsonQuote.getString(Constants.J_EPS) : "";
        this.lastTrade = jsonQuote.has(Constants.J_LAST_TRADE_TIME) ? jsonQuote.getString(Constants.J_LAST_TRADE_TIME) : "";
        this.stockExchange = jsonQuote.has(Constants.J_STOCK_EXCHANGE) ? jsonQuote.getString(Constants.J_STOCK_EXCHANGE) : "";

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
