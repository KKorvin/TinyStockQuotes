package org.linuxspace.stockquotes.view.cotroller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

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


public class ActivityMain extends ActionBarActivity implements SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener, AdapterView.OnItemClickListener {

    private ListView lvMainListview;
    private FinanceItemsAdapter financeItemsAdapter;
    private SearchAutoCompleterAdapter searchAutoCompleterAdapter;
    private SearchView mSearchView;
    private SlidingMenu slidingMenu;
    private MenuItem editMenuItem;
    private Toolbar mActionBarToolbar;
    private IQuotesGetterCallback gotQuotesCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);
        this.slidingMenu = new SlidingMenu(this);
        this.populateMainListview();
        this.searchAutoCompleterAdapter = new SearchAutoCompleterAdapter(this);
    }

    private void populateMainListview() {
        if (lvMainListview == null) {
            lvMainListview = (ListView) findViewById(R.id.lvFinanceItemsList);
            lvMainListview.setOnItemClickListener(this);
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
        MenuItem searchItem = menu.findItem(R.id.action_search);
        editMenuItem = menu.findItem(R.id.action_edit);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(searchItem, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        lvMainListview.setAdapter(financeItemsAdapter);
        editMenuItem.setVisible(true);
        mActionBarToolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_orange));
        populateMainListview();
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = slidingMenu.mDrawerLayout.isDrawerOpen(slidingMenu.mDrawerList);
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
    public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
        if (((ListView) listView).getAdapter() instanceof SearchAutoCompleterAdapter) {
            String clickedStockSymbol = searchAutoCompleterAdapter.getItem(position).symbol;
            ImageView imgAddFavorite = (ImageView) view.findViewById(R.id.imgAddFavorite);
            if (PreferencesManager.getInstance().stocksSetContains(this, clickedStockSymbol)) {
                PreferencesManager.getInstance().removeStockSymbolFromPrefs(this, clickedStockSymbol);
                imgAddFavorite.setImageResource(android.R.drawable.star_big_off);
            } else {
                PreferencesManager.getInstance().addStockSymbolToPrefs(this, clickedStockSymbol);
                imgAddFavorite.setImageResource(android.R.drawable.star_big_on);
            }
        } else {
            Intent startIntent = new Intent(this, ActivityDetails.class);
            startActivity(startIntent);
        }
    }
}
