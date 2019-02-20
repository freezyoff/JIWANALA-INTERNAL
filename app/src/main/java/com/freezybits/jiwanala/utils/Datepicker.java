package com.freezybits.jiwanala.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;

import java.util.Calendar;

public class Datepicker {

    public static DatePickerDialog createDatepicker(Context context,
                                                    View[] trigger,
                                                    DatePickerDialog.OnDateSetListener listener) {
        return createDatepicker(context, trigger, listener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE));
    }

    public static DatePickerDialog createDatepicker(Context context,
                                                    View[] trigger,
                                                    DatePickerDialog.OnDateSetListener listener,
                                                    int year,
                                                    int month,
                                                    int date) {
        return createDatepicker(context, 0, trigger, listener, year, month, date);
    }

    public static DatePickerDialog createDatepicker(Context context,
                                                    int themeResId,
                                                    View[] trigger,
                                                    DatePickerDialog.OnDateSetListener listener,
                                                    int year,
                                                    int month,
                                                    int date) {
        final DatePickerDialog dialog = new DatePickerDialog(context, themeResId, listener, year, month, date);

        //add onclick listener
        for (View view : trigger) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                }
            });
        }

        return dialog;
    }
}
