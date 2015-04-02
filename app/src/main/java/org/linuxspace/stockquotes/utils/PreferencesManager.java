package org.linuxspace.stockquotes.utils;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Alon on 23.03.2015.
 */
public class PreferencesManager {

    private static PreferencesManager preferencesManager;
    private TinyDB tinyDB;


    public static PreferencesManager getInstance() {
        if (preferencesManager == null) {
            preferencesManager = new PreferencesManager();
        }
        return preferencesManager;
    }

    public void init(Context appContext) {
        tinyDB = new TinyDB(appContext);
    }

    public void addStockSymbolToPrefs(String stockSymbol) {
        ArrayList<String> stocksList = tinyDB.getList(Constants.PREF_STOCKS_LIST);
        stocksList.add(stockSymbol);
        tinyDB.putList(Constants.PREF_STOCKS_LIST, stocksList);
    }

    public void removeStockSymbolFromPrefs(String stockSymbol) {
        ArrayList<String> stocksList = tinyDB.getList(Constants.PREF_STOCKS_LIST);
        stocksList.remove(stockSymbol);
        tinyDB.putList(Constants.PREF_STOCKS_LIST, stocksList);
    }

    public boolean stocksSetContains(String stockSymbol) {
        ArrayList<String> stocksList = tinyDB.getList(Constants.PREF_STOCKS_LIST);
        return stocksList.contains(stockSymbol);
    }

    public void saveStockList(ArrayList<String> stocksList) {
        tinyDB.putList(Constants.PREF_STOCKS_LIST, stocksList);
    }

    public ArrayList<String> getStockList() {
        return tinyDB.getList(Constants.PREF_STOCKS_LIST);
    }

    public boolean contains(String key) {
        return tinyDB.contains(key);
    }
}
