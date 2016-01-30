package org.linuxspace.stockquotes.model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.linuxspace.stockquotes.utils.JsonXmlConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

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
                news.title = jsonItem.getString(JsonXmlConstants.XML_TITLE);
                news.date = jsonItem.getString(JsonXmlConstants.XML_PUB_DATE);
                news.url = jsonItem.getString(JsonXmlConstants.XML_LINK);
                allNews.add(news);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allNews;
    }

    public static ArrayList<News> fromXML(Document xmlDocument) {
        ArrayList<News> allNews = new ArrayList<News>();
        try {
            NodeList itemsList = xmlDocument.getDocumentElement().getElementsByTagName(JsonXmlConstants.XML_ITEM);
            for (int i = 0; i < itemsList.getLength(); i++) {
                News news = new News();
                Node item = itemsList.item(i);
                NodeList properties = item.getChildNodes();
                for (int j = 0; j < properties.getLength(); j++) {
                    Node property = properties.item(j);
                    String name = property.getNodeName();
                    if (name.equalsIgnoreCase(JsonXmlConstants.XML_TITLE)) {
                        news.title = property.getFirstChild().getNodeValue();
                    } else if (name.equalsIgnoreCase(JsonXmlConstants.XML_LINK)) {
                        news.url = property.getFirstChild().getNodeValue();
                        if (news.url.contains("*") && news.url.lastIndexOf("http") > 0) {//contains 2 links -> take second
                            String[] urls = news.url.split(Pattern.quote("*"));
                            if (urls.length > 0) {
                                news.url = urls[1];
                            }
                        }
                    } else if (name.equalsIgnoreCase(JsonXmlConstants.XML_PUB_DATE)) {
                        news.date = property.getFirstChild().getNodeValue();
                    }
                }
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
