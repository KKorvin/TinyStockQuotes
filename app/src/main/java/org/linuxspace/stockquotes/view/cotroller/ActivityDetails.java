package org.linuxspace.stockquotes.view.cotroller;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.linuxspace.stockquotes.R;
import org.linuxspace.stockquotes.controller.HistoricalDataGetter;
import org.linuxspace.stockquotes.controller.NewsAdapter;
import org.linuxspace.stockquotes.controller.NewsGetter;
import org.linuxspace.stockquotes.controller.StockDetailsAdapter;
import org.linuxspace.stockquotes.model.News;
import org.linuxspace.stockquotes.model.Stock;
import org.linuxspace.stockquotes.model.StockDetailsItem;
import org.linuxspace.stockquotes.model.interfaces.IHistoricalDataGetterCallback;
import org.linuxspace.stockquotes.model.interfaces.INewsGetterCallback;

import java.util.ArrayList;
import java.util.Collections;

public class ActivityDetails extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private static final int GRAPHIC_FILL_ALPHA = 230;
    private static final float GRAPHIC_CUBIC_INTENSITY = 0.1f;
    private static final float GRAPHIC_LINE_WIDTH = 1f;

    public static enum GraphicType {
        YEAR, MONTH, WEEK
    }

    public static final String INTENT_EXTRA_URL = "newsUrl";

    private static Stock currentStock;

    private Toolbar mActionBarToolbar;
    private IHistoricalDataGetterCallback callback;
    private GraphicType currentGraphicType;
    private LineChart mChart;
    private ListView lvLeftDetailsColumn;
    private ListView lvRightDetailsColumn;
    private ListView lvNews;
    private NewsAdapter newsAdapter;
    private ProgressBarCircularIndeterminate pbNewsLoadingBar;
    private TextView tvGraphicLabelMonth;
    private TextView tvGraphicLabelYear;
    private TextView tvGraphicLabelWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (getIntent().hasExtra(ActivityMain.INTENT_EXTRA_STOCK)) {
            currentStock = (Stock) getIntent().getSerializableExtra(ActivityMain.INTENT_EXTRA_STOCK);
        }
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
        tvGraphicLabelMonth = (TextView) findViewById(R.id.tvGraphicLabelMonth);
        tvGraphicLabelYear = (TextView) findViewById(R.id.tvGraphicLabelYear);
        tvGraphicLabelWeek = (TextView) findViewById(R.id.tvGraphicLabelWeek);

        currentGraphicType = GraphicType.MONTH;
        setTopCard();
        setStockDetailsColumns();
        setGraphic();
        setNews();
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
                    try {
                        ArrayList<String> xVals = new ArrayList<String>();

                        ArrayList<Entry> yVals = new ArrayList<Entry>();
                        for (int i = 0; i < historicalData.size(); i++) {
                            xVals.add(String.valueOf(i));
                            yVals.add(new Entry(historicalData.get(i), i));
                        }
                        LineDataSet historicalDataSet = new LineDataSet(yVals, currentStock.name);
                        historicalDataSet.setDrawCircles(false);
                        historicalDataSet.setDrawCubic(true);
                        historicalDataSet.setDrawFilled(true);
                        historicalDataSet.setFillAlpha(GRAPHIC_FILL_ALPHA);
                        historicalDataSet.setCubicIntensity(GRAPHIC_CUBIC_INTENSITY);
                        historicalDataSet.setLineWidth(GRAPHIC_LINE_WIDTH);
                        historicalDataSet.setFillColor(getResources().getColor(R.color.toolbar_orange));
                        historicalDataSet.setColor(getResources().getColor(R.color.status_bar_orange));
                        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
                        dataSets.add(historicalDataSet);

                        LineData graphicLineData = new LineData(xVals, dataSets);
                        graphicLineData.setDrawValues(false);

                        YAxis mainYAxis = mChart.getAxisLeft();
                        mainYAxis.removeAllLimitLines();
                        mainYAxis.setAxisMaxValue(Collections.max(historicalData));
                        mainYAxis.setAxisMinValue(Collections.min(historicalData));
                        mainYAxis.setStartAtZero(false);

                        mChart.getAxisRight().setEnabled(false);
                        mChart.setBackgroundColor(Color.WHITE);
                        mChart.setDrawBorders(false);
                        mChart.setDragEnabled(false);
                        mChart.setTouchEnabled(false);
                        mChart.setPinchZoom(false);
                        mChart.setScaleEnabled(false);
                        mChart.setDrawGridBackground(false);
                        mChart.setDescription("");
                        mChart.setData(graphicLineData);
                        mChart.getLegend();
                        mChart.invalidate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }
        new HistoricalDataGetter(callback, currentStock.symbol, currentGraphicType).execute();
    }

    private void setNews() {
        lvNews = (ListView) findViewById(R.id.lvNews);
        lvNews.setOnItemClickListener(this);
        pbNewsLoadingBar = (ProgressBarCircularIndeterminate) findViewById(R.id.pbLoadingNews);
        INewsGetterCallback newsCallback = new INewsGetterCallback() {
            @Override
            public void onQuotesReceived(ArrayList<News> newsItems) {
                newsAdapter = new NewsAdapter(ActivityDetails.this, newsItems);
                pbNewsLoadingBar.setVisibility(View.GONE);
                lvNews.setAdapter(newsAdapter);
                View listItem = newsAdapter.getView(0, null, lvNews);
                listItem.measure(0, 0);
                float totalHeight = 0;
                for (int i = 0; i < newsAdapter.getCount(); i++) {
                    totalHeight += listItem.getMeasuredHeight();
                }
                ViewGroup.LayoutParams params = lvNews.getLayoutParams();
                params.height = (int) (totalHeight + (lvNews.getDividerHeight() * (lvNews.getCount() - 1)));
                lvNews.setLayoutParams(params);
                lvNews.requestLayout();
            }
        };
        new NewsGetter(newsCallback, currentStock.symbol).execute();
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onClickGraphicLabel(View v) {
        tvGraphicLabelMonth.setTextColor(getResources().getColor(R.color.sliding_menu_text_color));
        tvGraphicLabelWeek.setTextColor(getResources().getColor(R.color.sliding_menu_text_color));
        tvGraphicLabelYear.setTextColor(getResources().getColor(R.color.sliding_menu_text_color));

        tvGraphicLabelMonth.setTextSize(14);
        tvGraphicLabelWeek.setTextSize(14);
        tvGraphicLabelYear.setTextSize(14);

        tvGraphicLabelMonth.setTypeface(null, Typeface.NORMAL);
        tvGraphicLabelWeek.setTypeface(null, Typeface.NORMAL);
        tvGraphicLabelYear.setTypeface(null, Typeface.NORMAL);

        ((TextView) v).setTypeface(null, Typeface.BOLD);
        ((TextView) v).setTextSize(16);
        ((TextView) v).setTextColor(getResources().getColor(R.color.symbol_black));

        switch (v.getId()) {
            case R.id.tvGraphicLabelMonth: {
                currentGraphicType = GraphicType.MONTH;
                break;
            }
            case R.id.tvGraphicLabelWeek: {
                currentGraphicType = GraphicType.WEEK;
                break;
            }
            case R.id.tvGraphicLabelYear: {
                currentGraphicType = GraphicType.YEAR;
                break;
            }

        }

        setGraphic();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News clickedNews = newsAdapter.getItem(position);
        Intent startIntent = new Intent(this, ActivityWebview.class);
        startIntent.putExtra(INTENT_EXTRA_URL, clickedNews.url);
        startActivity(startIntent);
    }
}
