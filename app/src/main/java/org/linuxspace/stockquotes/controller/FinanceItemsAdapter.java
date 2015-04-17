package org.linuxspace.stockquotes.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nhaarman.listviewanimations.util.Swappable;

import org.linuxspace.stockquotes.R;
import org.linuxspace.stockquotes.model.FinanceItem;
import org.linuxspace.stockquotes.model.Stock;
import org.linuxspace.stockquotes.utils.PreferencesManager;
import org.linuxspace.stockquotes.view.cotroller.ActivityMain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by Alon on 13.03.2015.
 */
public class FinanceItemsAdapter extends ArrayAdapter<FinanceItem> implements Swappable {

    private static class ViewHolder {
        TextView tvName;
        TextView tvSymbol;
        TextView tvPrice;
        TextView tvPriceChange;
        TextView tvStockLetter;
        View viewPriceIndicator;
        LinearLayout llRemoveCheckMark;
    }

    private Context context;
    private ArrayList<FinanceItem> financeItems;


    public FinanceItemsAdapter(Context context, ArrayList<FinanceItem> financeItems) {
        super(context, R.layout.lv_main_item, financeItems);
        this.context = context;
        this.financeItems = financeItems;
        clearRemoveModes();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FinanceItem financeItem = getItem(position);
        ViewHolder viewHolder;
        //TODO ActivityMain.mode == ActivityMain.Mode.REMOVE - Bad temporary solution
        if (convertView == null || ActivityMain.mode == ActivityMain.Mode.REMOVE) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.lv_main_item, parent, false);
            viewHolder.tvSymbol = (TextView) convertView.findViewById(R.id.tvStockSymbol);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tvStockPrice);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvStockName);
            viewHolder.tvPriceChange = (TextView) convertView.findViewById(R.id.tvStockPriceChange);
            viewHolder.tvStockLetter = (TextView) convertView.findViewById(R.id.tvStockLetter);
            viewHolder.viewPriceIndicator = convertView.findViewById(R.id.viewPriceIndicator);
            viewHolder.llRemoveCheckMark = (LinearLayout) convertView.findViewById(R.id.llRemoveCheckMark);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (financeItem instanceof Stock) {
            viewHolder.tvName.setText(((Stock) financeItem).name);
            viewHolder.tvSymbol.setText(((Stock) financeItem).symbol);
            viewHolder.tvPrice.setText(((Stock) financeItem).price);
            viewHolder.tvPriceChange.setText(financeItem.getFormatedPriceChange());
            viewHolder.tvPriceChange.setTextColor(financeItem.getPriceColor(context));
            viewHolder.tvStockLetter.setText(((Stock) financeItem).getBigLetter());
            viewHolder.tvStockLetter.setTextColor(financeItem.getPriceColor(context));
            viewHolder.viewPriceIndicator.setBackgroundColor(financeItem.getPriceColor(context));
            if (financeItem.isRemoveMode) {
                viewHolder.tvStockLetter.setVisibility(View.GONE);
                viewHolder.llRemoveCheckMark.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvStockLetter.setVisibility(View.VISIBLE);
                viewHolder.llRemoveCheckMark.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    public void clearRemoveModes() {
        for (FinanceItem item : financeItems) {
            item.isRemoveMode = false;
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public void removeItems(HashSet<Integer> financeItemsToRemove) {
        for (int position : financeItemsToRemove) {
            FinanceItem financeItem = getItem(position);
            PreferencesManager.getInstance().removeStockSymbolFromPrefs(((Stock) financeItem).symbol);
            remove(financeItem);
        }
    }

    @Override
    public void swapItems(int i1, int i2) {
        Collections.swap(financeItems, i1, i2);
        notifyDataSetChanged();
    }

    public void saveOrder() {
        ArrayList<String> stocksList = new ArrayList<String>();
        for (FinanceItem item : financeItems) {
            stocksList.add(((Stock) item).symbol);
        }
        PreferencesManager.getInstance().saveStockList(stocksList);
    }

    public void orderByAlphabet() {
        Collections.sort(financeItems);
    }
}
