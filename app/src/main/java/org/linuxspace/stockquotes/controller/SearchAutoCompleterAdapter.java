package org.linuxspace.stockquotes.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.util.Swappable;

import org.json.JSONArray;
import org.linuxspace.stockquotes.R;
import org.linuxspace.stockquotes.model.SearchAutocompleteItem;
import org.linuxspace.stockquotes.utils.Constants;
import org.linuxspace.stockquotes.utils.GlobalUtils;
import org.linuxspace.stockquotes.utils.PreferencesManager;

import java.util.ArrayList;

/**
 * Created by Alon on 14.03.2015.
 */
public class SearchAutoCompleterAdapter extends BaseAdapter
        implements Filterable, Swappable {


    private class ViewHolder {
        TextView tvStockName;
        TextView getTvStockSymbol;
        ImageView imgFavorite;
    }

    private ArrayList<SearchAutocompleteItem> autocompleteItems;
    private Context context;
    private JSONArray nasdaqQuotes;

    public SearchAutoCompleterAdapter(Context context) {
        super();
        this.autocompleteItems = new ArrayList<SearchAutocompleteItem>();
        this.context = context;
        try {
            this.nasdaqQuotes = new JSONArray(GlobalUtils.readJsonStringFromFile(this.context, Constants.FILE_ALL_NASDAQ_QUOTES));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    autocomplete(constraint.toString());
                    filterResults.values = autocompleteItems;
                    filterResults.count = autocompleteItems.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private void autocomplete(String input) {
        autocompleteItems.clear();
        try {
            for (int i = 0; i < this.nasdaqQuotes.length(); i++) {
                SearchAutocompleteItem item = null;
                if (input.equalsIgnoreCase(nasdaqQuotes.getJSONObject(i).getString(Constants.J_AUTOCOMLETE_SYMBOL)) ||
                        input.equalsIgnoreCase(nasdaqQuotes.getJSONObject(i).getString(Constants.J_AUTOCOMLETE_NAME))) {
                    item = new SearchAutocompleteItem(nasdaqQuotes.getJSONObject(i));
                    item.order = SearchAutocompleteItem.MAX_ORDER;
                } else if (GlobalUtils.containsIgnoreCase(nasdaqQuotes.getJSONObject(i).getString(Constants.J_AUTOCOMLETE_SYMBOL), input) ||
                        GlobalUtils.containsIgnoreCase(nasdaqQuotes.getJSONObject(i).getString(Constants.J_AUTOCOMLETE_NAME), input)) {
                    item = new SearchAutocompleteItem(nasdaqQuotes.getJSONObject(i));
                    item.order = SearchAutocompleteItem.MIN_ORDER;
                }
                if (item != null) {
                    autocompleteItems.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getCount() {
        return autocompleteItems.size();
    }

    @Override
    public SearchAutocompleteItem getItem(int position) {
        return autocompleteItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchAutocompleteItem searchAutocompleteItem = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.lv_search_item, parent, false);
            viewHolder.getTvStockSymbol = (TextView) convertView.findViewById(R.id.tvStockSymbol);
            viewHolder.tvStockName = (TextView) convertView.findViewById(R.id.tvStockName);
            viewHolder.imgFavorite = (ImageView) convertView.findViewById(R.id.imgAddFavorite);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvStockName.setText(searchAutocompleteItem.name);
        viewHolder.getTvStockSymbol.setText(searchAutocompleteItem.symbol);
        if (PreferencesManager.getInstance().stocksSetContains(context, searchAutocompleteItem.symbol)) {
            viewHolder.imgFavorite.setImageResource(R.drawable.img_checkmark_orange);
        } else {
            viewHolder.imgFavorite.setImageResource(R.drawable.img_checkmark);
        }
        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void swapItems(int i, int i2) {

    }
}
