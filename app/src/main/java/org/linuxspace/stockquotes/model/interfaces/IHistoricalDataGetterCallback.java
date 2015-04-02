package org.linuxspace.stockquotes.model.interfaces;

import java.util.ArrayList;

/**
 * Created by Alon on 15.01.2015.
 */
public interface IHistoricalDataGetterCallback {

    public void onQuotesReceived(ArrayList<Float> financeItems);
}
