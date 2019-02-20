package com.freezybits.jiwanala.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.freezybits.jiwanala.R;
import com.freezybits.jiwanala.utils.Datepicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DashboardActivity extends AppCompatActivity {

    TextView txDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txDate = findViewById(R.id.txdate);
        SimpleDateFormat format = new SimpleDateFormat("MMMM YYYY");
        txDate.setText(format.format(Calendar.getInstance().getTime()));

        View[] trigger = {txDate};
        DatePickerDialog dialog = Datepicker.createDatepicker(this, trigger, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cl = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("MMMM YYYY");
                txDate.setText(format.format(cl.getTime()));
            }
        });
    }
}
