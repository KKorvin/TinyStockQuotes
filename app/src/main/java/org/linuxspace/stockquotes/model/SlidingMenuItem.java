package org.linuxspace.stockquotes.model;

import android.content.Context;

import org.linuxspace.stockquotes.R;

import java.util.ArrayList;

/**
 * Created by Alon on 20.03.2015.
 */
public class SlidingMenuItem {

    public String title;
    public int iconId;

    public SlidingMenuItem(String title, int iconId) {
        this.title = title;
        this.iconId = iconId;
    }

    public static ArrayList<SlidingMenuItem> fromDefaultSlidingMenuSet(Context mContext) {
        ArrayList<SlidingMenuItem> allItems = new ArrayList<SlidingMenuItem>();
        allItems.add(new SlidingMenuItem(mContext.getString(R.string.seetings), R.drawable.ic_settings));
        allItems.add(new SlidingMenuItem(mContext.getString(R.string.ratee_app), android.R.drawable.ic_menu_add));
        return allItems;
    }

}
