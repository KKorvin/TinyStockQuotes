package org.linuxspace.stockquotes.utils;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Alon on 23.03.2015.
 */
public class PreferencesManager {

    public static final String PREF_STOCKS_LIST = "stocksList";

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
        ArrayList<String> stocksList = tinyDB.getList(PREF_STOCKS_LIST);
        stocksList.add(stockSymbol);
        tinyDB.putList(PREF_STOCKS_LIST, stocksList);
    }

    public void removeStockSymbolFromPrefs(String stockSymbol) {
        ArrayList<String> stocksList = tinyDB.getList(PREF_STOCKS_LIST);
        stocksList.remove(stockSymbol);
        tinyDB.putList(PREF_STOCKS_LIST, stocksList);
    }

    public boolean stocksSetContains(String stockSymbol) {
        ArrayList<String> stocksList = tinyDB.getList(PREF_STOCKS_LIST);
        return stocksList.contains(stockSymbol);
    }

    public void saveStockList(ArrayList<String> stocksList) {
        tinyDB.putList(PREF_STOCKS_LIST, stocksList);
    }

    public void saveBoolean(String key, boolean value) {
        tinyDB.putBoolean(key, value);
    }

    public boolean getBoolean(String key) {
        return tinyDB.getBoolean(key);
    }

    public boolean hasBoolean(String key) {
        return tinyDB.contains(key);
    }

    public ArrayList<String> getStockList() {
        return tinyDB.getList(PREF_STOCKS_LIST);
    }

    public boolean contains(String key) {
        return tinyDB.contains(key);
    }
}
