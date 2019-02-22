package com.freezybits.jiwanala.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

public class ViewUtils {

    public static Drawable getDrawable(Context context, int resourceDrawable) {
        Drawable icon = context.getResources().getDrawable(resourceDrawable);
        icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
        return icon;
    }

    public static LinearLayout.LayoutParams createLinearLayoutParams(boolean widthMatchParent, boolean heightMatchParent) {
        return new LinearLayout.LayoutParams(
                widthMatchParent ? LinearLayout.LayoutParams.MATCH_PARENT : LinearLayout.LayoutParams.WRAP_CONTENT,
                heightMatchParent ? LinearLayout.LayoutParams.MATCH_PARENT : LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public static TableLayout.LayoutParams createTableLayoutParams(boolean widthMatchParent, boolean heightMatchParent) {
        return new TableLayout.LayoutParams(
                widthMatchParent ? TableLayout.LayoutParams.MATCH_PARENT : TableLayout.LayoutParams.WRAP_CONTENT,
                heightMatchParent ? TableLayout.LayoutParams.MATCH_PARENT : TableLayout.LayoutParams.WRAP_CONTENT);
    }

    public static TableRow.LayoutParams createTableRowLayoutParams(boolean widthMatchParent, boolean heightMatchParent) {
        return new TableRow.LayoutParams(
                widthMatchParent ? TableRow.LayoutParams.MATCH_PARENT : TableRow.LayoutParams.WRAP_CONTENT,
                heightMatchParent ? TableRow.LayoutParams.MATCH_PARENT : TableRow.LayoutParams.WRAP_CONTENT);
    }
}
