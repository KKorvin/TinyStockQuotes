package org.linuxspace.stockquotes;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import org.linuxspace.stockquotes.utils.Constants;

import java.util.HashSet;
import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Alon on 23.03.2015.
 */
public class ExtendedApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES_FILE, Context.MODE_PRIVATE);
        if (!prefs.contains(Constants.PREF_STOCKS_LIST)) {
            Set<String> stocksSet = new HashSet<String>();
            stocksSet.add("AAPL");
            stocksSet.add("GOOG");
            stocksSet.add("YHOO");
            SharedPreferences.Editor editor = prefs.edit();
            editor.putStringSet(Constants.PREF_STOCKS_LIST, stocksSet);
            editor.commit();
        }
    }
}
