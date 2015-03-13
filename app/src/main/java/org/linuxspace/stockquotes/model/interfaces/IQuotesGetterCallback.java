package org.linuxspace.stockquotes.model.interfaces;

import org.linuxspace.stockquotes.model.FinanceItem;

import java.util.ArrayList;

/**
 * Created by Alon on 15.01.2015.
 */
public interface IQuotesGetterCallback {

    public void onQuotesReceived(ArrayList<FinanceItem> financeItems);
}
