package org.linuxspace.stockquotes.model;

import android.content.Context;

import org.linuxspace.stockquotes.R;

import java.util.ArrayList;

/**
 * Created by Alon on 20.03.2015.
 */
public class StockDetailsItem {

    public String title;
    public String value;

    public StockDetailsItem(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public static ArrayList<StockDetailsItem> fromDefaulrLeftColumn(Context mContext, Stock stock) {
        ArrayList<StockDetailsItem> allItems = new ArrayList<StockDetailsItem>();
        allItems.add(new StockDetailsItem(mContext.getString(R.string.open), stock.open));
        allItems.add(new StockDetailsItem(mContext.getString(R.string.volume), stock.volume));
        allItems.add(new StockDetailsItem(mContext.getString(R.string.hight), stock.hight));
        allItems.add(new StockDetailsItem(mContext.getString(R.string.low), stock.low));
        return allItems;
    }

    public static ArrayList<StockDetailsItem> fromDefaulrRightColumn(Context mContext, Stock stock) {
        ArrayList<StockDetailsItem> allItems = new ArrayList<StockDetailsItem>();
        allItems.add(new StockDetailsItem(mContext.getString(R.string.prev_close), stock.prevClose));
        allItems.add(new StockDetailsItem(mContext.getString(R.string.last_trade), stock.lastTrade));
        allItems.add(new StockDetailsItem(mContext.getString(R.string.exchange), stock.stockExchange));
        allItems.add(new StockDetailsItem(mContext.getString(R.string.eps), stock.eps));
        return allItems;
    }
}
