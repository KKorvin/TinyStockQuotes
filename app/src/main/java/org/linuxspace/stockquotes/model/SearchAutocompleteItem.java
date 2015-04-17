package org.linuxspace.stockquotes.model;

import org.json.JSONObject;
import org.linuxspace.stockquotes.utils.JsonConstants;

/**
 * Created by Alon on 14.03.2015.
 */
public class SearchAutocompleteItem {

    public static final int MAX_ORDER = 10;
    public static final int MIN_ORDER = 0;
    public String name;
    public String symbol;
    public int order;

    public SearchAutocompleteItem(JSONObject jsonObject) {
        try {
            this.name = jsonObject.getString(JsonConstants.J_AUTOCOMLETE_NAME);
            this.symbol = jsonObject.getString(JsonConstants.J_AUTOCOMLETE_SYMBOL);
            this.order = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
