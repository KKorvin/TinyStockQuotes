package org.linuxspace.stockquotes.model;

import android.content.Context;

import org.linuxspace.stockquotes.R;

/**
 * Basic class for any finance item (i.e stock, currency)
 */
public abstract class FinanceItem {
    public String lastUpdate;
    public String stockExchange;
    public String price;
    public String priceChangePercent;
    public String priceChangeNumber;

    public String getFormatedPriceChange() {
        return this.priceChangeNumber + " (" + this.priceChangePercent + ")";
    }

    public int getPriceColor(Context context) {
        return this.priceChangeNumber.contains("-") ? context.getResources().getColor(R.color.price_red) : context.getResources().getColor(R.color.price_green);
    }
}
