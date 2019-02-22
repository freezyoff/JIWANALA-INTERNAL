package com.freezybits.jiwanala.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;

import java.util.Calendar;

public class Datepicker {

    public static DatePickerDialog createDatepicker(Context context,
                                                    View[] trigger,
                                                    DatePickerDialog.OnDateSetListener listener) {
        Calendar cl = Calendar.getInstance();
        return createDatepicker(context,
                trigger,
                listener,
                cl.get(Calendar.YEAR),
                cl.get(Calendar.MONTH),
                cl.get(Calendar.DATE));
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
                                                    DatePickerDialog.OnDateSetListener listener) {
        Calendar cl = Calendar.getInstance();
        return createDatepicker(context, themeResId, trigger, listener, cl.get(Calendar.YEAR),
                cl.get(Calendar.MONTH),
                cl.get(Calendar.DATE));

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
