package org.linuxspace.stockquotes;

import android.app.Application;

import org.linuxspace.stockquotes.utils.Constants;
import org.linuxspace.stockquotes.utils.PreferencesManager;

import java.util.ArrayList;

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
        PreferencesManager.getInstance().init(getApplicationContext());
        if (!PreferencesManager.getInstance().contains(Constants.PREF_STOCKS_LIST)) {
            ArrayList<String> stocksList = new ArrayList<String>();
            stocksList.add("AAPL");
            stocksList.add("GOOG");
            stocksList.add("YHOO");
            PreferencesManager.getInstance().saveStockList(stocksList);
        }
    }
}
