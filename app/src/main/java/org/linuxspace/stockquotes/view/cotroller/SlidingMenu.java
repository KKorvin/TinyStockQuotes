package org.linuxspace.stockquotes.view.cotroller;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.linuxspace.stockquotes.R;
import org.linuxspace.stockquotes.controller.SlidinMenuAdapter;
import org.linuxspace.stockquotes.model.SlidingMenuItem;

/**
 * Created by Alon on 20.03.2015.
 */
public class SlidingMenu {

    private ActionBarActivity activity;
    public DrawerLayout mDrawerLayout;
    public ListView mDrawerList;
    public ActionBarDrawerToggle mDrawerToggle;

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    public SlidingMenu(ActionBarActivity mActivity) {
        this.activity = mActivity;

        this.mDrawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        this.mDrawerList = (ListView) activity.findViewById(R.id.left_drawer);
        this.mDrawerList.setAdapter(new SlidinMenuAdapter(activity, SlidingMenuItem.fromDefaultSlidingMenuSet(activity)));
        this.mDrawerToggle = new ActionBarDrawerToggle(activity, mDrawerLayout, (Toolbar) activity.findViewById(R.id.toolbar_actionbar), R.drawable.abc_ic_clear_mtrl_alpha, R.drawable.abc_ic_clear_mtrl_alpha) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                activity.invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                activity.invalidateOptionsMenu();
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

}
