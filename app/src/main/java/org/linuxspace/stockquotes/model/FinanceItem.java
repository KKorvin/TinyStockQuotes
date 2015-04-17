package org.linuxspace.stockquotes.model;

import android.content.Context;

import org.linuxspace.stockquotes.R;

import java.io.Serializable;

/**
 * Basic class for any finance item (i.e stock, currency)
 */
public abstract class FinanceItem implements Comparable<FinanceItem>,Serializable {
    public String lastUpdate;
    public String stockExchange;
    public String price;
    public String priceChangePercent;
    public String priceChangeNumber;
    public boolean isRemoveMode;

    public String getFormatedPriceChange() {
        return this.priceChangeNumber + " (" + this.priceChangePercent + ")";
    }

    public int getPriceColor(Context context) {
        return this.priceChangeNumber.contains("-") ? context.getResources().getColor(R.color.price_red) : context.getResources().getColor(R.color.price_green);
    }

    public int compareTo(FinanceItem item) {
        return 0;
    }
}
