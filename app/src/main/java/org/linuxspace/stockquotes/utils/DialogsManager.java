package org.linuxspace.stockquotes.utils;

import android.content.Context;
import android.text.Html;
import android.view.View;

import org.linuxspace.stockquotes.R;

import java.util.HashMap;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Alon on 27.03.2015.
 */
public class DialogsManager {

    private static final String WELCOME_SHOWN = "welcom_shown";
    private static Map<String, String> allLibreries = new HashMap();

    static {
        //All used open source libreries
        allLibreries.put("Calligraphy", "https://github.com/chrisjenx/Calligraphy");
        allLibreries.put("MaterialDesignLibrary", "https://github.com/navasmdc/MaterialDesignLibrary");
        allLibreries.put("MaterialDialog", "https://github.com/drakeet/MaterialDialog");
        allLibreries.put("ListViewAnimations", "https://github.com/nhaarman/ListViewAnimations");
        allLibreries.put("MPAndroidChart", "https://github.com/PhilJay/MPAndroidChart");
        allLibreries.put("NineOldAndroids", "https://github.com/JakeWharton/NineOldAndroids/");
        allLibreries.put("MPAndroidChart", "https://github.com/PhilJay/MPAndroidChart");
        allLibreries.put("AndroidViewAnimations", "https://github.com/daimajia/AndroidViewAnimations");

    }

    public static void showCopyrightDialog(Context mContext) {
        StringBuilder stringBuilder = new StringBuilder("<b>Open source libraries:</b><br>");
        for (Map.Entry<String, String> entry : allLibreries.entrySet()) {
            stringBuilder.append("<a href='" + entry.getValue() + "'" + ">" + entry.getKey() + "<a><br>");
        }
        stringBuilder.append("<br><b>Free icons:</b><br>");
        stringBuilder.append("<a href='http://icons8.com/android-L/'" + ">icons8<a>");
        final MaterialDialog mMaterialDialog = new MaterialDialog(mContext);
        mMaterialDialog.setTitle(R.string.copyright);
        mMaterialDialog.setMessage(Html.fromHtml(stringBuilder.toString()));
        mMaterialDialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        mMaterialDialog.show();
    }

    public static void showWelcomeDialog(Context mContext) {
        if (PreferencesManager.getInstance().hasBoolean(WELCOME_SHOWN))
            return;
        final MaterialDialog mMaterialDialog = new MaterialDialog(mContext);
        mMaterialDialog.setTitle(R.string.welcome);
        mMaterialDialog.setMessage(R.string.welcome_message);
        mMaterialDialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        mMaterialDialog.show();
        PreferencesManager.getInstance().saveBoolean(WELCOME_SHOWN, true);
    }

}
