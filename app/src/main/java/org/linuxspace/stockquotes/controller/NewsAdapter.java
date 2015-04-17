package org.linuxspace.stockquotes.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.linuxspace.stockquotes.R;
import org.linuxspace.stockquotes.model.News;

import java.util.ArrayList;

/**
 * Created by Alon on 13.03.2015.
 */
public class NewsAdapter extends ArrayAdapter<News> {

    private static class ViewHolder {
        TextView tvTitle;
        TextView tvDate;
    }

    public NewsAdapter(Context context, ArrayList<News> newsItems) {
        super(context, R.layout.lv_drawer_item, newsItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        News newsItem = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.lv_news_item, parent, false);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvNewsTitle);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvNewsDate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTitle.setText(newsItem.title);
        viewHolder.tvDate.setText(newsItem.getFormatedDate());

        return convertView;
    }
}
