package org.linuxspace.stockquotes.model.interfaces;

import org.linuxspace.stockquotes.model.News;

import java.util.ArrayList;

/**
 * Created by Alon on 15.01.2015.
 */
public interface INewsGetterCallback {

    public void onQuotesReceived(ArrayList<News> newsItems);
}
