package org.linuxspace.stockquotes.model;

/**
 * Basic class for any finance item (i.e stock, currency)
 */
public abstract class FinanceItem {
    public String lastUpdate;
    public String stockExchange;
    public String price;
    public String priceChangePercent;
    public String priceChangeNumber;
}
