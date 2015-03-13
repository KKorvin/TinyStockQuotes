package org.linuxspace.stockquotes.utils;

/**
 * Created by Alon on 13.03.2015.
 */
public class Constants {

    //Yahoo urls
    public static final String BASIC_YAHOO_API_URL = "http://query.yahooapis.com/v1/public/yql?";
    public static final String YAHOO_DB_URL = "store://datatables.org/alltableswithkeys";

    // time in miliseconds
    public static final int URL_CONNECTION_TIME_OUT = 15000;
    public static final int INPUT_STREAM_READ_TIME_OUT = 10000;

    //JSON Constants
    public static final String J_QUERY = "query";
    public static final String J_RESULTS = "results";
    public static final String J_QUOTE = "quote";
    public static final String J_SYMBOL_SMALL = "symbol";
    public static final String J_SYMBOL_BIG = "Symbol";
    public static final String J_LAST_TRADE_PRICE = "LastTradePriceOnly";
    public static final String J_LAST_TRADE_TIME = "LastTradeTime";
    public static final String J_CHANGE_NUMBER = "Change";
    public static final String J_CHANGE_PERCENT = "PercentChange";
    public static final String J_VOLUME = "Volume";
    public static final String J_STOCK_EXCHANGE = "StockExchange";
    public static final String J_NAME = "Name";

    //Strings
    public static final String UNKNOWN = "N/A";

}
