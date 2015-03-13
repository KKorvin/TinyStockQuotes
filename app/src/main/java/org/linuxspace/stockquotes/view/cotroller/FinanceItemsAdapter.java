package org.linuxspace.stockquotes.view.cotroller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.linuxspace.stockquotes.R;
import org.linuxspace.stockquotes.model.FinanceItem;
import org.linuxspace.stockquotes.model.Stock;

import java.util.ArrayList;

/**
 * Created by Alon on 13.03.2015.
 */
public class FinanceItemsAdapter extends ArrayAdapter<FinanceItem> {

    // View lookup cache
    private static class ViewHolder {
        TextView tvName;
        TextView tvSymbol;
        TextView tvPrice;
    }

    public FinanceItemsAdapter(Context context, ArrayList<FinanceItem> financeItems) {
        super(context, R.layout.main_listview_finance_item, financeItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FinanceItem financeItem = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.main_listview_finance_item, parent, false);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvStockName);
            viewHolder.tvSymbol = (TextView) convertView.findViewById(R.id.tvStockSymbol);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tvStockPrice);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (financeItem instanceof Stock) {
            viewHolder.tvName.setText(((Stock) financeItem).name);
            viewHolder.tvSymbol.setText(((Stock) financeItem).symbol);
            viewHolder.tvPrice.setText(((Stock) financeItem).price);
        }
        return convertView;
    }
}
