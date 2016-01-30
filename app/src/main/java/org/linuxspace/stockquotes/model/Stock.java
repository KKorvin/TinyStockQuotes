package org.linuxspace.stockquotes.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.linuxspace.stockquotes.utils.JsonXmlConstants;

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
        this.isRemoveMode = false;
        this.name = jsonQuote.has(JsonXmlConstants.J_NAME) ? jsonQuote.getString(JsonXmlConstants.J_NAME) : "";
        this.symbol = jsonQuote.has(JsonXmlConstants.J_SYMBOL_SMALL) ? jsonQuote.getString(JsonXmlConstants.J_SYMBOL_SMALL) : "";
        if (this.symbol.isEmpty()) {
            this.symbol = jsonQuote.has(JsonXmlConstants.J_SYMBOL_BIG) ? jsonQuote.getString(JsonXmlConstants.J_SYMBOL_BIG) : "";
        }
        this.price = jsonQuote.has(JsonXmlConstants.J_LAST_TRADE_PRICE) ? jsonQuote.getString(JsonXmlConstants.J_LAST_TRADE_PRICE) : UNKNOWN;
        this.lastUpdate = jsonQuote.has(JsonXmlConstants.J_LAST_TRADE_TIME) ? jsonQuote.getString(JsonXmlConstants.J_LAST_TRADE_TIME) : "";
        this.priceChangeNumber = jsonQuote.has(JsonXmlConstants.J_CHANGE_NUMBER) ? jsonQuote.getString(JsonXmlConstants.J_CHANGE_NUMBER) : "";
        this.priceChangePercent = jsonQuote.has(JsonXmlConstants.J_CHANGE_PERCENT) ? jsonQuote.getString(JsonXmlConstants.J_CHANGE_PERCENT) : "";
        this.stockExchange = jsonQuote.has(JsonXmlConstants.J_STOCK_EXCHANGE) ? jsonQuote.getString(JsonXmlConstants.J_STOCK_EXCHANGE) : "";
        this.volume = jsonQuote.has(JsonXmlConstants.J_VOLUME) ? jsonQuote.getString(JsonXmlConstants.J_VOLUME) : "";
        this.open = jsonQuote.has(JsonXmlConstants.J_OPEN) ? jsonQuote.getString(JsonXmlConstants.J_OPEN) : "";
        this.prevClose = jsonQuote.has(JsonXmlConstants.J_PREV_CLOSE) ? jsonQuote.getString(JsonXmlConstants.J_PREV_CLOSE) : "";
        this.hight = jsonQuote.has(JsonXmlConstants.J_HIGTH) ? jsonQuote.getString(JsonXmlConstants.J_HIGTH) : "";
        this.low = jsonQuote.has(JsonXmlConstants.J_LOW) ? jsonQuote.getString(JsonXmlConstants.J_LOW) : "";
        this.eps = jsonQuote.has(JsonXmlConstants.J_EPS) ? jsonQuote.getString(JsonXmlConstants.J_EPS) : "";
        this.lastTrade = jsonQuote.has(JsonXmlConstants.J_LAST_TRADE_TIME) ? jsonQuote.getString(JsonXmlConstants.J_LAST_TRADE_TIME) : "";
        this.stockExchange = jsonQuote.has(JsonXmlConstants.J_STOCK_EXCHANGE) ? jsonQuote.getString(JsonXmlConstants.J_STOCK_EXCHANGE) : "";

        this.name = this.name.equals("null") || this.name.equals("Null") ? UNKNOWN : this.name;
        this.eps = this.eps.equals("null") || this.eps.equals("Null") ? UNKNOWN : this.eps;
        this.prevClose = this.prevClose.equals("null") || this.eps.equals("Null") ? UNKNOWN : this.prevClose;
        this.hight = this.hight.equals("null") || this.hight.equals("Null") ? UNKNOWN : this.hight;
        this.low = this.low.equals("null") || this.low.equals("Null") ? UNKNOWN : this.low;
        this.lastTrade = this.lastTrade.equals("null") || this.lastTrade.equals("Null") ? UNKNOWN : this.lastTrade;
        this.stockExchange = this.stockExchange.equals("null") || this.stockExchange.equals("Null") ? UNKNOWN : this.stockExchange;
        this.open = this.open.equals("null") || this.open.equals("Null") ? UNKNOWN : this.open;

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
