package com.freezybits.jiwanala.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

public class ViewUtils {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static Drawable getDrawable(Context context, int resourceDrawable) {
        Drawable icon = context.getResources().getDrawable(resourceDrawable);
        icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
        return icon;
    }

    /**
     * @param unit - see {@link TypedValue}
     * @param size - desired size
     * @return float translated to dimension
     */
    public static int transDimension(int unit, float size) {
        return Math.round(TypedValue.applyDimension(unit, size, Resources.getSystem().getDisplayMetrics()));
    }


    public static LinearLayout.LayoutParams createLinearLayoutParams(boolean widthMatchParent, boolean heightMatchParent) {
        return createLinearLayoutParams(widthMatchParent, heightMatchParent, 0);
    }

    public static LinearLayout.LayoutParams createLinearLayoutParams(boolean widthMatchParent, boolean heightMatchParent, float weight) {
        return new LinearLayout.LayoutParams(
                widthMatchParent ? LinearLayout.LayoutParams.MATCH_PARENT : LinearLayout.LayoutParams.WRAP_CONTENT,
                heightMatchParent ? LinearLayout.LayoutParams.MATCH_PARENT : LinearLayout.LayoutParams.WRAP_CONTENT,
                weight);
    }

    public static TableLayout.LayoutParams createTableLayoutParams(boolean widthMatchParent, boolean heightMatchParent) {
        return createTableLayoutParams(widthMatchParent, heightMatchParent, 0);
    }

    public static TableLayout.LayoutParams createTableLayoutParams(boolean widthMatchParent, boolean heightMatchParent, float weight) {
        return new TableLayout.LayoutParams(
                widthMatchParent ? TableLayout.LayoutParams.MATCH_PARENT : TableLayout.LayoutParams.WRAP_CONTENT,
                heightMatchParent ? TableLayout.LayoutParams.MATCH_PARENT : TableLayout.LayoutParams.WRAP_CONTENT,
                weight);
    }

    public static TableRow.LayoutParams createTableRowLayoutParams(boolean widthMatchParent, boolean heightMatchParent) {
        return createTableRowLayoutParams(widthMatchParent, heightMatchParent, 0);
    }

    public static TableRow.LayoutParams createTableRowLayoutParams(boolean widthMatchParent, boolean heightMatchParent, float weight) {
        return new TableRow.LayoutParams(
                widthMatchParent ? TableRow.LayoutParams.MATCH_PARENT : TableRow.LayoutParams.WRAP_CONTENT,
                heightMatchParent ? TableRow.LayoutParams.MATCH_PARENT : TableRow.LayoutParams.WRAP_CONTENT,
                weight);
    }
}
