package org.linuxspace.stockquotes;

import android.app.Application;

import org.linuxspace.stockquotes.utils.PreferencesManager;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Alon on 23.03.2015.
 */
public class ExtendedApplication extends Application {

    private static final String ROBOTO_FONT_PATH = "fonts/Roboto-Regular.ttf";

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath(ROBOTO_FONT_PATH)
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        PreferencesManager.getInstance().init(getApplicationContext());
        if (!PreferencesManager.getInstance().contains(PreferencesManager.PREF_STOCKS_LIST)) {
            ArrayList<String> stocksList = new ArrayList<String>();
            stocksList.add("AAPL");
            stocksList.add("GOOG");
            stocksList.add("YHOO");
            PreferencesManager.getInstance().saveStockList(stocksList);
        }
    }
}
