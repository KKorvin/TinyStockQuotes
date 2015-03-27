package org.linuxspace.stockquotes.view.cotroller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import org.linuxspace.stockquotes.R;
import org.linuxspace.stockquotes.controller.FinanceItemsAdapter;
import org.linuxspace.stockquotes.controller.QuotesGetter;
import org.linuxspace.stockquotes.controller.SearchAutoCompleterAdapter;
import org.linuxspace.stockquotes.model.FinanceItem;
import org.linuxspace.stockquotes.model.interfaces.IQuotesGetterCallback;
import org.linuxspace.stockquotes.utils.Constants;
import org.linuxspace.stockquotes.utils.PreferencesManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ActivityMain extends ActionBarActivity implements SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemLongClickListener {

    private Toolbar mActionBarToolbar;
    private MenuItem editMenuItem;
    private MenuItem microMenuItem;
    private MenuItem removeMenuItem;
    private MenuItem searchMenuItem;
    private SearchView mSearchView;

    private SlidingMenu slidingMenu;

    private DynamicListView lvMainListview;
    private SwipeRefreshLayout srQuotesRefresher;
    private ProgressBarCircularIndeterminate pbLoadingQuotes;

    private FinanceItemsAdapter financeItemsAdapter;
    private SearchAutoCompleterAdapter searchAutoCompleterAdapter;
    private IQuotesGetterCallback gotQuotesCallback;

    private boolean isEditMode;
    private boolean isSearchMode;
    private HashSet<Integer> financeItemsToRemove;

    View.OnTouchListener tlMainListview = new View.OnTouchListener() {
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == android.view.MotionEvent.ACTION_UP && !isSearchMode && !isEditMode) {
                srQuotesRefresher.setEnabled(true);
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.financeItemsToRemove = new HashSet<Integer>();
        srQuotesRefresher = (SwipeRefreshLayout) findViewById(R.id.srQuotesRefresher);
        srQuotesRefresher.setOnRefreshListener(this);
        srQuotesRefresher.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        pbLoadingQuotes = (ProgressBarCircularIndeterminate) findViewById(R.id.pbLoadingQuotes);
        setSupportActionBar(mActionBarToolbar);
        this.slidingMenu = new SlidingMenu(this);
        this.populateMainListview();
        this.searchAutoCompleterAdapter = new SearchAutoCompleterAdapter(this);
    }

    private void populateMainListview() {
        if (lvMainListview == null) {
            lvMainListview = (DynamicListView) findViewById(R.id.lvFinanceItemsList);
            lvMainListview.enableDragAndDrop();
            lvMainListview.setOnItemClickListener(this);
            lvMainListview.setOnItemLongClickListener(this);
            lvMainListview.setOnTouchListener(tlMainListview);
            if (!srQuotesRefresher.isRefreshing()) {
                pbLoadingQuotes.setVisibility(View.VISIBLE);
            }
            gotQuotesCallback = new IQuotesGetterCallback() {
                @Override
                public void onQuotesReceived(ArrayList<FinanceItem> financeItems) {
                    if (financeItemsAdapter == null) {
                        financeItemsAdapter = new FinanceItemsAdapter(ActivityMain.this, financeItems);
                    } else {
                        financeItemsAdapter.clear();
                        financeItemsAdapter.addAll(financeItems);
                        financeItemsAdapter.notifyDataSetChanged();
                    }
                    lvMainListview.setAdapter(financeItemsAdapter);
                    pbLoadingQuotes.setVisibility(View.GONE);
                    srQuotesRefresher.setRefreshing(false);
                }
            };
        }
        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES_FILE, Context.MODE_PRIVATE);
        Set<String> stocksSet = prefs.getStringSet(Constants.PREF_STOCKS_LIST, new HashSet<String>());
        new QuotesGetter(gotQuotesCallback, stocksSet.toArray(new String[stocksSet.size()])).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        microMenuItem = menu.findItem(R.id.action_micro);
        microMenuItem.setVisible(false);
        removeMenuItem = menu.findItem(R.id.action_remove);
        removeMenuItem.setVisible(false);
        searchMenuItem = menu.findItem(R.id.action_search);
        editMenuItem = menu.findItem(R.id.action_edit);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.equals(microMenuItem)) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.search_hint));
            startActivityForResult(intent, Constants.MICROPHONE_REQUEST_CODE);
        } else if (item.equals(editMenuItem)) {
            removeMenuItem.setVisible(true);
            searchMenuItem.setVisible(false);
            editMenuItem.setVisible(false);
            slidingMenu.mDrawerToggle.setDrawerIndicatorEnabled(false);
            slidingMenu.mDrawerLayout.setEnabled(false);
            slidingMenu.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mActionBarToolbar.setLogo(R.drawable.icon_toolbar_checked);
            mActionBarToolbar.setTitle(Constants.TOOLBAR_REMOVEMODE_SPACES + "0 " + getString(R.string.from) + " " + String.valueOf(financeItemsAdapter.getCount()));
            financeItemsToRemove.clear();
            isEditMode = true;
            financeItemsAdapter.isEditMode = true;
            srQuotesRefresher.setEnabled(false);
        } else if (item.equals(removeMenuItem)) {
            removeMenuItem.setVisible(false);
            editMenuItem.setVisible(true);
            searchMenuItem.setVisible(true);
            slidingMenu.mDrawerToggle.setDrawerIndicatorEnabled(true);
            slidingMenu.mDrawerLayout.setEnabled(true);
            slidingMenu.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mActionBarToolbar.setLogo(null);
            mActionBarToolbar.setTitle(getString(R.string.app_name));
            isEditMode = false;
            financeItemsAdapter.isEditMode = false;
            financeItemsAdapter.removeItems(financeItemsToRemove);
            financeItemsAdapter.notifyDataSetChanged();
            srQuotesRefresher.setEnabled(true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String queryText) {
        searchAutoCompleterAdapter.getFilter().filter(queryText);
        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        lvMainListview.setAdapter(searchAutoCompleterAdapter);
        editMenuItem.setVisible(false);
        microMenuItem.setVisible(true);
        slidingMenu.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        isSearchMode = true;
        srQuotesRefresher.setEnabled(false);
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        lvMainListview.setAdapter(financeItemsAdapter);
        editMenuItem.setVisible(true);
        slidingMenu.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        microMenuItem.setVisible(false);
        mActionBarToolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_orange));
        populateMainListview();
        isSearchMode = false;
        srQuotesRefresher.setEnabled(false);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //boolean drawerOpen = slidingMenu.mDrawerLayout.isDrawerOpen(slidingMenu.mDrawerList);
        //menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        //menu.findItem(R.id.action_edit).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        slidingMenu.mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        slidingMenu.mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch (keycode) {
            case KeyEvent.KEYCODE_MENU:
                slidingMenu.toogle();
                return true;
        }

        return super.onKeyDown(keycode, e);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
        if (((ListView) listView).getAdapter() instanceof SearchAutoCompleterAdapter) {
            String clickedStockSymbol = searchAutoCompleterAdapter.getItem(position).symbol;
            ImageView imgAddFavorite = (ImageView) view.findViewById(R.id.imgAddFavorite);
            if (PreferencesManager.getInstance().stocksSetContains(this, clickedStockSymbol)) {
                PreferencesManager.getInstance().removeStockSymbolFromPrefs(this, clickedStockSymbol);
                imgAddFavorite.setImageResource(R.drawable.img_checkmark);
            } else {
                PreferencesManager.getInstance().addStockSymbolToPrefs(this, clickedStockSymbol);
                imgAddFavorite.setImageResource(R.drawable.img_checkmark_orange);
            }
        } else {
            if (isEditMode) {
                TextView tvStockLetter = (TextView) view.findViewById(R.id.tvStockLetter);
                LinearLayout llRemoveCheckMark = (LinearLayout) view.findViewById(R.id.llRemoveCheckMark);
                llRemoveCheckMark.setVisibility(llRemoveCheckMark.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                tvStockLetter.setVisibility(tvStockLetter.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                if (llRemoveCheckMark.getVisibility() == View.VISIBLE) {
                    financeItemsToRemove.add(position);
                } else {
                    financeItemsToRemove.remove(position);
                }
                mActionBarToolbar.setTitle(Constants.TOOLBAR_REMOVEMODE_SPACES + String.valueOf(financeItemsToRemove.size()) + " " + getString(R.string.from) + " " + String.valueOf(financeItemsAdapter.getCount()));
            } else {
                Intent startIntent = new Intent(this, ActivityDetails.class);
                startActivity(startIntent);
            }
        }
    }

    @Override
    public void onRefresh() {
        populateMainListview();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.MICROPHONE_REQUEST_CODE) {
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            if (!matches.isEmpty()) {
                mSearchView.setQuery(matches.get(0), true);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (isEditMode) {
            onOptionsItemSelected(removeMenuItem);
        } else if (isSearchMode) {
            searchMenuItem.collapseActionView();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (!isEditMode && !isSearchMode) {
            srQuotesRefresher.setEnabled(false);
            lvMainListview.startDragging(position);
        }
        return true;
    }


}
