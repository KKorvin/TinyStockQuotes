package org.linuxspace.stockquotes.view.cotroller;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.linuxspace.stockquotes.R;
import org.linuxspace.stockquotes.controller.HistoricalDataGetter;
import org.linuxspace.stockquotes.controller.StockDetailsAdapter;
import org.linuxspace.stockquotes.model.Stock;
import org.linuxspace.stockquotes.model.StockDetailsItem;
import org.linuxspace.stockquotes.model.interfaces.IHistoricalDataGetterCallback;
import org.linuxspace.stockquotes.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;

public class ActivityDetails extends ActionBarActivity {


    public static enum GraphicType {
        YEAR, MONTH, WEEK
    }


    private Toolbar mActionBarToolbar;
    private Stock currentStock;
    private IHistoricalDataGetterCallback callback;
    private GraphicType currentGraphicType;
    private LineChart mChart;
    private ListView lvLeftDetailsColumn;
    private ListView lvRightDetailsColumn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        currentStock = (Stock) getIntent().getSerializableExtra(Constants.INTENT_EXTRA_STOCK);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        mActionBarToolbar.setTitle(R.string.stock_details);
        mActionBarToolbar.setNavigationIcon(R.drawable.icon_toolbal_arrow_white);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(mActionBarToolbar);
        currentGraphicType = GraphicType.MONTH;
        setTopCard();
        setStockDetailsColumns();
        setGraphic();

    }

    private void setTopCard() {
        ((TextView) findViewById(R.id.tvStockName)).setText(currentStock.name);
        ((TextView) findViewById(R.id.tvStockSymbol)).setText(currentStock.symbol);
        TextView tvStockPrice = (TextView) findViewById(R.id.tvStockPrice);
        TextView tvStockPriceChange = (TextView) findViewById(R.id.tvStockPriceChange);
        View viewPriceIndicator = (View) findViewById(R.id.viewPriceIndicator);
        tvStockPrice.setText(currentStock.price);
        tvStockPriceChange.setText(currentStock.getFormatedPriceChange());
        tvStockPriceChange.setTextColor(currentStock.getPriceColor(this));
        viewPriceIndicator.setBackgroundColor(currentStock.getPriceColor(this));
    }

    private void setStockDetailsColumns() {
        lvLeftDetailsColumn = (ListView) findViewById(R.id.lvStockDetailsLeft);
        lvLeftDetailsColumn.setEnabled(false);
        StockDetailsAdapter leftAdapter = new StockDetailsAdapter(this, StockDetailsItem.fromDefaulrLeftColumn(this, currentStock));
        lvLeftDetailsColumn.setAdapter(leftAdapter);

        lvRightDetailsColumn = (ListView) findViewById(R.id.lvStockDetailsRight);
        lvRightDetailsColumn.setEnabled(false);
        StockDetailsAdapter rightAdapter = new StockDetailsAdapter(this, StockDetailsItem.fromDefaulrRightColumn(this, currentStock));
        lvRightDetailsColumn.setAdapter(rightAdapter);
    }

    private void setGraphic() {
        if (mChart == null) {
            mChart = (LineChart) findViewById(R.id.chartStock);
        }
        if (callback == null) {
            callback = new IHistoricalDataGetterCallback() {
                @Override
                public void onQuotesReceived(ArrayList<Float> historicalData) {
                    ArrayList<String> xVals = new ArrayList<String>();
                    /*if (currentGraphicType == GraphicType.MONTH) {
                        xVals.add(getString(R.string.week) + " 1");
                        xVals.add(getString(R.string.week) + " 2");
                        xVals.add(getString(R.string.week) + " 3");
                        xVals.add(getString(R.string.week) + " 4");
                    }*/

                    ArrayList<Entry> yVals = new ArrayList<Entry>();
                    for (int i = 0; i < historicalData.size(); i++) {
                        xVals.add(String.valueOf(i));
                        yVals.add(new Entry(historicalData.get(i), i));
                    }
                    LineDataSet historicalDataSet = new LineDataSet(yVals, Constants.GRAPHIC_DATA_SET);
                    historicalDataSet.setDrawFilled(true);
                    historicalDataSet.setFillColor(getResources().getColor(R.color.toolbar_orange));
                    historicalDataSet.setColor(getResources().getColor(R.color.status_bar_orange));
                    historicalDataSet.setFillAlpha(80);
                    ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
                    dataSets.add(historicalDataSet);

                    LineData graphicLineData = new LineData(xVals, dataSets);

                    /*LimitLine llUpper = new LimitLine(130f, "Upper Limit");
                    llUpper.setLineWidth(4f);
                    llUpper.setLabelPosition(LimitLine.LimitLabelPosition.POS_RIGHT);
                    llUpper.setTextSize(10f);

                    LimitLine llLower = new LimitLine(-30f, "Lower Limit");
                    llLower.setLineWidth(4f);
                    llLower.enableDashedLine(10f, 10f, 0f);
                    llLower.setLabelPosition(LimitLine.LimitLabelPosition.POS_RIGHT);
                    llLower.setTextSize(10f);*/

                    YAxis leftAxis = mChart.getAxisLeft();
                    //leftAxis.removeAllLimitLines();
                    //leftAxis.addLimitLine(llUpper);
                    //leftAxis.addLimitLine(llLower);
                    leftAxis.setAxisMaxValue(Collections.max(historicalData));
                    leftAxis.setAxisMinValue(Collections.min(historicalData));
                    leftAxis.setStartAtZero(false);

                    mChart.getAxisRight().setEnabled(false);
                    mChart.setData(graphicLineData);
                    mChart.invalidate();
                }
            };
        }
        new HistoricalDataGetter(callback, currentStock.symbol, currentGraphicType).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //slidingMenu.mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
