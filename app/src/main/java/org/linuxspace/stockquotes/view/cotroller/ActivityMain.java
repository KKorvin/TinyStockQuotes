package org.linuxspace.stockquotes.view.cotroller;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.linuxspace.stockquotes.R;
import org.linuxspace.stockquotes.controller.QuotesGetter;
import org.linuxspace.stockquotes.model.FinanceItem;
import org.linuxspace.stockquotes.model.interfaces.IQuotesGetterCallback;

import java.util.ArrayList;


public class ActivityMain extends ActionBarActivity {

    private ListView lvMainListview;
    private FinanceItemsAdapter financeItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.populateMainListview();
    }

    private void populateMainListview(){
        lvMainListview = (ListView) findViewById(R.id.lvFinnceItemsList);
        IQuotesGetterCallback callback = new IQuotesGetterCallback() {
            @Override
            public void onQuotesReceived(ArrayList<FinanceItem> financeItems) {
                financeItemsAdapter = new FinanceItemsAdapter(ActivityMain.this, financeItems);
                lvMainListview.setAdapter(financeItemsAdapter);
            }
        };
        String stocks[] = {"AAPL", "GOOG", "YHOO"};
        new QuotesGetter(callback, stocks).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
