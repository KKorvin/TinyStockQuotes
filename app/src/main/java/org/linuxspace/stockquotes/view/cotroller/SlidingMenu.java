package org.linuxspace.stockquotes.view.cotroller;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.linuxspace.stockquotes.R;
import org.linuxspace.stockquotes.controller.SlidinMenuAdapter;
import org.linuxspace.stockquotes.model.SlidingMenuItem;
import org.linuxspace.stockquotes.utils.DialogsManager;
import org.linuxspace.stockquotes.utils.GlobalUtils;

/**
 * Created by Alon on 20.03.2015.
 */
public class SlidingMenu implements ListView.OnItemClickListener {

    private static final String FEEDBACK_EMAIL = "alon.milo@gmail.com";
    private static final String GITHUB_URL = "https://github.com/KKorvin/MaterialStockQuotes";
    private ActionBarActivity activity;
    public DrawerLayout mDrawerLayout;
    public ListView mDrawerList;
    public ActionBarDrawerToggle mDrawerToggle;

    public View.OnClickListener exitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            activity.finish();
        }
    };


    public SlidingMenu(ActionBarActivity mActivity) {
        this.activity = mActivity;
        this.mDrawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        this.mDrawerList = (ListView) activity.findViewById(R.id.left_drawer);
        this.mDrawerList.setAdapter(new SlidinMenuAdapter(activity, SlidingMenuItem.fromDefaultSlidingMenuSet(activity)));
        this.mDrawerToggle = new ActionBarDrawerToggle(activity, mDrawerLayout,
                (Toolbar) activity.findViewById(R.id.toolbar_actionbar),
                R.string.sliding_menu, R.string.sliding_menu) {

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
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View footer = inflater.inflate(R.layout.lv_drawer_footer, mDrawerList, false);
        footer.setOnClickListener(exitClickListener);
        mDrawerList.addFooterView(footer);
        mDrawerList.setOnItemClickListener(this);
    }

    public void toogle() {
        if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        } else {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mDrawerLayout.closeDrawer(mDrawerList);
        SlidingMenuItem clickedItem = (SlidingMenuItem) (mDrawerList.getItemAtPosition(position));
        if (clickedItem.title.equals(activity.getString(R.string.share))) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.share_text) + " " + GlobalUtils.buildGooglePlayLink(activity));
            activity.startActivity(Intent.createChooser(sharingIntent, activity.getString(R.string.share)));
        } else if (clickedItem.title.equals(activity.getString(R.string.ratee_app))) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName())));
        } else if (clickedItem.title.equals(activity.getString(R.string.copyright))) {
            DialogsManager.showCopyrightDialog(activity);
        } else if (clickedItem.title.equals(activity.getString(R.string.feedback))) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/html");
            intent.putExtra(Intent.EXTRA_EMAIL, FEEDBACK_EMAIL);
            activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.send_email)));
        } else if (clickedItem.title.equals(activity.getString(R.string.feedback))) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(GITHUB_URL));
            activity.startActivity(i);
        }
    }


}
