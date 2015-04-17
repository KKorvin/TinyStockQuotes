package org.linuxspace.stockquotes.model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.linuxspace.stockquotes.utils.JsonConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alon on 02.04.2015.
 */
public class News {
    public String date;
    public String title;
    public String url;

    public static ArrayList<News> fromJson(JSONArray jsonItems) {
        ArrayList<News> allNews = new ArrayList<News>();
        try {
            for (int i = 0; i < jsonItems.length(); i++) {
                News news = new News();
                JSONObject jsonItem = (JSONObject) jsonItems.get(i);
                news.title = jsonItem.getString(JsonConstants.J_TITLE);
                news.date = jsonItem.getString(JsonConstants.J_PUB_DATE);
                news.url = jsonItem.getString(JsonConstants.J_LINK);
                allNews.add(news);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allNews;
    }

    public String getFormatedDate() {
        try {
            SimpleDateFormat dt = new SimpleDateFormat("dd MMMM");
            Date dateToFormat = new Date(this.date);
            return dt.format(dateToFormat).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
