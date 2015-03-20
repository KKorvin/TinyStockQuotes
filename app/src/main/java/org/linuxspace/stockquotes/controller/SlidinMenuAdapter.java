package org.linuxspace.stockquotes.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.linuxspace.stockquotes.R;
import org.linuxspace.stockquotes.model.SlidingMenuItem;

import java.util.ArrayList;

/**
 * Created by Alon on 13.03.2015.
 */
public class SlidinMenuAdapter extends ArrayAdapter<SlidingMenuItem> {

    // View lookup cache
    private static class ViewHolder {
        TextView tvTitle;
        ImageView imgIcon;
    }

    public SlidinMenuAdapter(Context context, ArrayList<SlidingMenuItem> slidingMenuItems) {
        super(context, R.layout.lv_drawer_item, slidingMenuItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SlidingMenuItem slidingMenuItem = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.lv_drawer_item, parent, false);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvtiltle);
            viewHolder.imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTitle.setText(slidingMenuItem.title);
        viewHolder.imgIcon.setImageResource(slidingMenuItem.iconId);

        return convertView;
    }
}
