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
        allItems.add(new SlidingMenuItem(mContext.getString(R.string.ratee_app), R.drawable.icon_slidingmenu_rate));
        allItems.add(new SlidingMenuItem(mContext.getString(R.string.share), R.drawable.icon_share));
        allItems.add(new SlidingMenuItem(mContext.getString(R.string.feedback), R.drawable.icon_slidingmenu_feedback));
        allItems.add(new SlidingMenuItem(mContext.getString(R.string.github), R.drawable.icon_slidingmenu_git));
        allItems.add(new SlidingMenuItem(mContext.getString(R.string.copyright), R.drawable.icon_slidingmenu_copyright));

        return allItems;
    }

}
