package org.linuxspace.stockquotes.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alon on 23.03.2015.
 */
public class PreferencesManager {

    private static PreferencesManager preferencesManager;

    public static PreferencesManager getInstance() {
        if (preferencesManager == null) {
            preferencesManager = new PreferencesManager();
        }
        return preferencesManager;
    }

    public void addStockSymbolToPrefs(Context mContext, String stockSymbol) {
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCES_FILE, Context.MODE_PRIVATE);
        Set<String> stocksSet = prefs.getStringSet(Constants.PREF_STOCKS_LIST, new HashSet<String>());
        stocksSet.add(stockSymbol);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putStringSet(Constants.PREF_STOCKS_LIST, stocksSet);
        editor.commit();
    }

    public void removeStockSymbolFromPrefs(Context mContext, String stockSymbol) {
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCES_FILE, Context.MODE_PRIVATE);
        Set<String> stocksSet = prefs.getStringSet(Constants.PREF_STOCKS_LIST, new HashSet<String>());
        stocksSet.remove(stockSymbol);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putStringSet(Constants.PREF_STOCKS_LIST, stocksSet);
        editor.commit();
    }

    public boolean stocksSetContains(Context mContext, String stockSymbol) {
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCES_FILE, Context.MODE_PRIVATE);
        Set<String> stocksSet = prefs.getStringSet(Constants.PREF_STOCKS_LIST, new HashSet<String>());
        return stocksSet.contains(stockSymbol);
    }

}
