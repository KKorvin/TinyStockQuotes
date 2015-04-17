package org.linuxspace.stockquotes.view.cotroller;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import org.linuxspace.stockquotes.R;
import org.linuxspace.stockquotes.controller.FinanceItemsAdapter;
import org.linuxspace.stockquotes.controller.QuotesGetter;
import org.linuxspace.stockquotes.controller.SearchAutoCompleterAdapter;
import org.linuxspace.stockquotes.model.FinanceItem;
import org.linuxspace.stockquotes.model.interfaces.IQuotesGetterCallback;
import org.linuxspace.stockquotes.utils.DialogsManager;
import org.linuxspace.stockquotes.utils.GlobalUtils;
import org.linuxspace.stockquotes.utils.PreferencesManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ActivityMain extends ActionBarActivity implements SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemLongClickListener {

    private static final int REMOVE_MODE_ANIMATION_DURATION = 250;
    private static final int MENU_ITEM_ANIMATION_DURATION = 850;
    private static final int MICROPHONE_REQUEST_CODE = 121;
    private static final String TOOLBAR_REMOVE_MODE_SPACES = "     ";

    public static final String INTENT_EXTRA_STOCK = "intentStock";

    public static enum Mode {
        NORMAL, REMOVE, SEARCH, SORT;
    }

    public static Mode mode;

    private Toolbar mActionBarToolbar;
    private SearchView mSearchView;
    private SlidingMenu slidingMenu;
    private TextView tvNoInternet;
    private TextView tvToolbarTitle;
    private ImageButton btnToolbarButton;

    private MenuItem editMenuItem;
    private MenuItem microMenuItem;
    private MenuItem removeMenuItem;
    private MenuItem searchMenuItem;
    private MenuItem sortAbMenuItem;

    private DynamicListView lvMainListview;
    private SwipeRefreshLayout srQuotesRefresher;
    private ProgressBarCircularIndeterminate pbLoadingQuotes;

    private FinanceItemsAdapter financeItemsAdapter;
    private SearchAutoCompleterAdapter searchAutoCompleterAdapter;
    private IQuotesGetterCallback gotQuotesCallback;

    private HashSet<Integer> financeItemsToRemove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mode = Mode.NORMAL;
        this.financeItemsToRemove = new HashSet<Integer>();
        srQuotesRefresher = (SwipeRefreshLayout) findViewById(R.id.srQuotesRefresher);
        srQuotesRefresher.setOnRefreshListener(this);
        srQuotesRefresher.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        pbLoadingQuotes = (ProgressBarCircularIndeterminate) findViewById(R.id.pbLoadingQuotes);
        tvNoInternet = (TextView) findViewById(R.id.tvNoInternet);
        setSupportActionBar(mActionBarToolbar);
        this.slidingMenu = new SlidingMenu(this);
        this.searchAutoCompleterAdapter = new SearchAutoCompleterAdapter(this);
        try {
            Field f = mActionBarToolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            tvToolbarTitle = (TextView) f.get(mActionBarToolbar);

            f = mActionBarToolbar.getClass().getDeclaredField("mNavButtonView");
            f.setAccessible(true);
            btnToolbarButton = (ImageButton) f.get(mActionBarToolbar);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        populateMainListview();
        DialogsManager.showWelcomeDialog(this);
    }

    private void populateMainListview() {
        if (!GlobalUtils.isNetworkConnected(this)) {
            srQuotesRefresher.setRefreshing(false);
            pbLoadingQuotes.setVisibility(View.GONE);
            tvNoInternet.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeInUp)
                    .duration(1000)
                    .playOn(tvNoInternet);
            return;
        } else if (tvNoInternet.getVisibility() == View.VISIBLE) {
            YoYo.with(Techniques.FadeInDown)
                    .duration(1000)
                    .playOn(tvNoInternet);
            tvNoInternet.setVisibility(View.GONE);
        }

        if (lvMainListview == null) {
            lvMainListview = (DynamicListView) findViewById(R.id.lvFinanceItemsList);
            lvMainListview.enableDragAndDrop();
            lvMainListview.setOnItemClickListener(this);
            lvMainListview.setOnItemLongClickListener(this);
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
        ArrayList<String> stocksList = PreferencesManager.getInstance().getStockList();
        new QuotesGetter(gotQuotesCallback, stocksList.toArray(new String[stocksList.size()])).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        microMenuItem = menu.findItem(R.id.action_micro);
        removeMenuItem = menu.findItem(R.id.action_remove);
        searchMenuItem = menu.findItem(R.id.action_search);
        editMenuItem = menu.findItem(R.id.action_edit);
        sortAbMenuItem = menu.findItem(R.id.action_sort);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, this);
        startMode(Mode.NORMAL);
        return true;
    }

    private void startMode(Mode modeToStart) {
        //Save order before exit sort mode
        if (mode == Mode.SORT) {
            financeItemsAdapter.saveOrder();
        }
        if (modeToStart == Mode.NORMAL) {
            srQuotesRefresher.setEnabled(true);
            removeMenuItem.setVisible(false);
            microMenuItem.setVisible(false);
            sortAbMenuItem.setVisible(false);
            searchMenuItem.setVisible(true);
            editMenuItem.setVisible(true);
            mActionBarToolbar.setLogo(null);
            mActionBarToolbar.setTitle(getString(R.string.app_name));
            mActionBarToolbar.setBackgroundResource(R.color.toolbar_orange);
            slidingMenu.mDrawerToggle.setDrawerIndicatorEnabled(true);
            slidingMenu.mDrawerLayout.setEnabled(true);
            slidingMenu.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            slidingMenu.mDrawerToggle.setHomeAsUpIndicator(null);
            if (mode != modeToStart) {
                GlobalUtils.safeAnimate(findViewById(searchMenuItem.getItemId()), MENU_ITEM_ANIMATION_DURATION, Techniques.FlipInX);
                GlobalUtils.safeAnimate(findViewById(editMenuItem.getItemId()), MENU_ITEM_ANIMATION_DURATION, Techniques.FlipInX);
                GlobalUtils.safeAnimate(tvToolbarTitle, MENU_ITEM_ANIMATION_DURATION, Techniques.FlipInX);
                GlobalUtils.safeAnimate(btnToolbarButton, MENU_ITEM_ANIMATION_DURATION, Techniques.FlipInX);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.status_bar_orange));
            }
            if (financeItemsAdapter != null) {
                lvMainListview.setAdapter(financeItemsAdapter);
            }
            mode = modeToStart;
            return;
        } else if (modeToStart == Mode.SORT) {
            removeMenuItem.setVisible(false);
            searchMenuItem.setVisible(false);
            editMenuItem.setVisible(false);
            microMenuItem.setVisible(false);
            sortAbMenuItem.setVisible(true);
            mActionBarToolbar.setTitle(getString(R.string.drag_drop));
            GlobalUtils.safeAnimate(findViewById(sortAbMenuItem.getItemId()), MENU_ITEM_ANIMATION_DURATION, Techniques.FlipInX);
            GlobalUtils.safeAnimate(tvToolbarTitle, MENU_ITEM_ANIMATION_DURATION, Techniques.FlipInX);
            mActionBarToolbar.setBackgroundResource(R.color.price_green);
            mActionBarToolbar.setLogo(null);
            slidingMenu.mDrawerToggle.setHomeAsUpIndicator(getV7DrawerToggleDelegate().getThemeUpIndicator());
            slidingMenu.mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startMode(Mode.NORMAL);
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.status_bar_green));
            }
        } else if (modeToStart == Mode.REMOVE) {
            searchMenuItem.setVisible(false);
            editMenuItem.setVisible(false);
            microMenuItem.setVisible(false);
            removeMenuItem.setVisible(true);
            GlobalUtils.safeAnimate(findViewById(removeMenuItem.getItemId()), MENU_ITEM_ANIMATION_DURATION, Techniques.FlipInX);
            mActionBarToolbar.setLogo(R.drawable.icon_toolbar_checked);
            mActionBarToolbar.setTitle(TOOLBAR_REMOVE_MODE_SPACES + "0 " + getString(R.string.from) + " " + String.valueOf(financeItemsAdapter.getCount()));
            GlobalUtils.safeAnimate(tvToolbarTitle, MENU_ITEM_ANIMATION_DURATION, Techniques.FlipInX);
            GlobalUtils.safeAnimate(btnToolbarButton, MENU_ITEM_ANIMATION_DURATION, Techniques.FlipInX);
            mActionBarToolbar.setBackgroundResource(R.color.price_red);
            financeItemsToRemove.clear();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.status_bar_red));
            }
        } else if (modeToStart == Mode.SEARCH) {
            removeMenuItem.setVisible(false);
            searchMenuItem.setVisible(false);
            editMenuItem.setVisible(false);
            microMenuItem.setVisible(true);
        }
        mode = modeToStart;
        srQuotesRefresher.setEnabled(false);
        slidingMenu.mDrawerToggle.setDrawerIndicatorEnabled(false);
        slidingMenu.mDrawerLayout.setEnabled(false);
        slidingMenu.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.equals(microMenuItem)) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.search_hint));
            startActivityForResult(intent, MICROPHONE_REQUEST_CODE);
        } else if (item.equals(editMenuItem)) {
            startMode(Mode.SORT);
        } else if (item.equals(removeMenuItem)) {
            startMode(Mode.NORMAL);
            financeItemsAdapter.removeItems(financeItemsToRemove);
            financeItemsAdapter.notifyDataSetChanged();
        } else if (item.equals(sortAbMenuItem)) {
            financeItemsAdapter.orderByAlphabet();
            financeItemsAdapter.saveOrder();
            financeItemsAdapter.notifyDataSetChanged();
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
        startMode(Mode.SEARCH);
        lvMainListview.setAdapter(searchAutoCompleterAdapter);
        slidingMenu.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        startMode(Mode.NORMAL);
        lvMainListview.setAdapter(financeItemsAdapter);
        populateMainListview();
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
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
            if (PreferencesManager.getInstance().stocksSetContains(clickedStockSymbol)) {
                PreferencesManager.getInstance().removeStockSymbolFromPrefs(clickedStockSymbol);
                imgAddFavorite.setImageResource(R.drawable.img_checkmark);
            } else {
                PreferencesManager.getInstance().addStockSymbolToPrefs(clickedStockSymbol);
                imgAddFavorite.setImageResource(R.drawable.img_checkmark_orange);
            }
        } else {
            if (mode == Mode.REMOVE) {
                markAsRemove(view, position);
            } else {
                Intent startIntent = new Intent(this, ActivityDetails.class);
                startIntent.putExtra(INTENT_EXTRA_STOCK, financeItemsAdapter.getItem(position));
                startActivity(startIntent);
            }
        }
    }

    private void markAsRemove(View view, int position) {
        final TextView tvStockLetter = (TextView) view.findViewById(R.id.tvStockLetter);
        final LinearLayout llRemoveCheckMark = (LinearLayout) view.findViewById(R.id.llRemoveCheckMark);
        if (llRemoveCheckMark.getVisibility() == View.GONE) {
            YoYo.with(Techniques.SlideOutLeft)
                    .duration(REMOVE_MODE_ANIMATION_DURATION)
                    .playOn(tvStockLetter);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tvStockLetter.setVisibility(View.GONE);
                            llRemoveCheckMark.setVisibility(View.VISIBLE);
                            YoYo.with(Techniques.SlideInLeft)
                                    .duration(REMOVE_MODE_ANIMATION_DURATION)
                                    .playOn(llRemoveCheckMark);
                        }
                    }, REMOVE_MODE_ANIMATION_DURATION);
                }
            });
            financeItemsToRemove.add(position);
        } else {
            YoYo.with(Techniques.SlideOutLeft)
                    .duration(REMOVE_MODE_ANIMATION_DURATION)
                    .playOn(llRemoveCheckMark);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            llRemoveCheckMark.setVisibility(View.GONE);
                            tvStockLetter.setVisibility(View.VISIBLE);
                            YoYo.with(Techniques.SlideInLeft)
                                    .duration(REMOVE_MODE_ANIMATION_DURATION)
                                    .playOn(tvStockLetter);
                        }
                    }, REMOVE_MODE_ANIMATION_DURATION);
                }
            });
            financeItemsToRemove.remove(position);
        }
        mActionBarToolbar.setTitle(TOOLBAR_REMOVE_MODE_SPACES + String.valueOf(financeItemsToRemove.size()) + " " + getString(R.string.from) + " " + String.valueOf(financeItemsAdapter.getCount()));
    }

    @Override
    public void onRefresh() {
        populateMainListview();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MICROPHONE_REQUEST_CODE) {
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
        if (mode == Mode.SEARCH) {
            searchMenuItem.collapseActionView();
        } else if (mode != Mode.NORMAL) {
            startMode(Mode.NORMAL);
        } else {
            finish();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (mode == Mode.NORMAL) {
            startMode(Mode.REMOVE);
            markAsRemove(view, position);
        } else if (mode == Mode.SORT) {
            lvMainListview.startDragging(position);
        } else if (mode == Mode.REMOVE) {
            markAsRemove(view, position);
        }
        return true;
    }


}
