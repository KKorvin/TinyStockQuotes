package org.linuxspace.stockquotes.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.linuxspace.stockquotes.model.interfaces.IHistoricalDataGetterCallback;
import org.linuxspace.stockquotes.utils.JsonConstants;
import org.linuxspace.stockquotes.utils.YahooApiUtils;
import org.linuxspace.stockquotes.view.cotroller.ActivityDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Alon on 15.01.2015.
 */
public class HistoricalDataGetter extends BasicAsyncTask {

    private IHistoricalDataGetterCallback callback;
    private String stocksSymbol;
    private ArrayList<Float> histroicalData;
    private ActivityDetails.GraphicType graphicType;

    public HistoricalDataGetter(IHistoricalDataGetterCallback callback, String stocksSymbol, ActivityDetails.GraphicType graphicType) {
        this.callback = callback;
        this.wasError = false;
        this.stocksSymbol = stocksSymbol;
        this.graphicType = graphicType;
        this.histroicalData = new ArrayList<Float>();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            String query = buildQuotesGetQuery();
            String url = YahooApiUtils.createUrlFromQury(query);
            JSONObject jsonQuery = getJsonWithUrl(url).getJSONObject(JsonConstants.J_QUERY);
            JSONObject jsonResult = jsonQuery.getJSONObject(JsonConstants.J_RESULTS);
            JSONObject jsonQuote = jsonResult.optJSONObject(JsonConstants.J_QUOTE);
            JSONArray jsonQuotes = new JSONArray();
            if (jsonQuote == null) {
                jsonQuotes = jsonResult.getJSONArray(JsonConstants.J_QUOTE);
            } else {
                jsonQuotes.put(jsonQuote);
            }
            for (int i = 0; i < jsonQuotes.length(); i++) {
                JSONObject jsonObject = jsonQuotes.getJSONObject(i);
                if (jsonObject.has(JsonConstants.J_CLOSE)) {
                    histroicalData.add((float) jsonObject.getDouble(JsonConstants.J_CLOSE));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        callback.onQuotesReceived(histroicalData);
        super.onPostExecute(aVoid);
    }

    private String buildQuotesGetQuery() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = new String(sdf.format(c.getTime()));
        switch (graphicType) {
            case WEEK: {
                c.add(Calendar.WEEK_OF_MONTH, -1);
                break;
            }
            case MONTH: {
                c.add(Calendar.MONTH, -1);
                break;
            }
            case YEAR: {
                c.add(Calendar.YEAR, -1);
                break;
            }
        }
        String maxDate = new String(sdf.format(c.getTime()));

        String query = "select * from yahoo.finance.historicaldata where symbol = '" + stocksSymbol + "' and startDate = '" +
                maxDate + "' and endDate = '" + todayDate + "'";
        return query;
    }
}
