package org.linuxspace.stockquotes.model;

/**
 * Basic class for any finance item (i.e stock, currency)
 */
public abstract class FinanceItem {
    public String lastUpdate;
    public String exchDisp;
    public float price;
    public float priceChangePercent;
    public float priceChange;
}
