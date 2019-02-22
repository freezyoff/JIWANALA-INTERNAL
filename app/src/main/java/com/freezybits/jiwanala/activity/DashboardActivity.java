package com.freezybits.jiwanala.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.freezybits.jiwanala.R;
import com.freezybits.jiwanala.foundation.SharedInstance;
import com.freezybits.jiwanala.foundation.http.ServerConnection;
import com.freezybits.jiwanala.foundation.http.ServerResponseListener;
import com.freezybits.jiwanala.foundation.http.ServerResponseParameters;
import com.freezybits.jiwanala.utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Iterator;

public class DashboardActivity extends AppCompatActivity {
    ArrayAdapter<String> monthsAdapater;
    ArrayAdapter<String> yearAdapter;
    Button btnMonth;
    Button btnYear;
    Calendar selectedPeriode = Calendar.getInstance();
    TableLayout tableAttendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dashboard);

        initFields();
        initViews();
        initServer();
    }

    protected void initServer() {
        ServerConnection connection = SharedInstance.getServerManager().getAttendanceHistoryConnection(
                selectedPeriode.get(Calendar.MONTH) + 1,
                selectedPeriode.get(Calendar.YEAR)
        );
        connection.addServerResponseListener(new ServerResponseListener() {
            @Override
            public void serverResponseAccepted(int responseCode, ServerResponseParameters parameters) {
                if (responseCode == 200) {
                    try {
                        tableAttendance.removeAllViews();
                        fillTableLayout(parameters.getJSONParameters("success"));
                        showDahboard();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        connection.execute();
    }

    protected void initFields() {
        monthsAdapater = new ArrayAdapter(this, android.R.layout.select_dialog_singlechoice);
        monthsAdapater.addAll(getResources().getStringArray(R.array.calendar_month_names));
        int year = Calendar.getInstance().get(Calendar.YEAR);
        yearAdapter = new ArrayAdapter(this, android.R.layout.select_dialog_singlechoice);
        for (int i = year - 2; i < year + 3; i++) {
            yearAdapter.add(i + "");
        }
    }

    protected void initViews() {
        tableAttendance = findViewById(R.id.table_attendance);

        int currentMonth = selectedPeriode.get(Calendar.MONTH) + 1;
        btnMonth = findViewById(R.id.btn_month);
        btnMonth.setText(monthsAdapater.getItem(currentMonth - 1));
        btnMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMonthsDialog();
            }
        });
        btnMonth.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        int year = selectedPeriode.get(Calendar.YEAR);
        btnYear = findViewById(R.id.btn_year);
        btnYear.setText(yearAdapter.getItem(yearAdapter.getPosition(year + "")));
        btnYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYearDialog();
            }
        });
        btnYear.setInputType(InputType.TYPE_CLASS_NUMBER);
        showDahboard();
    }

    void showDahboard() {
        toggleLoader(View.GONE);
        toggleDahsboard(View.VISIBLE);
    }

    void showLoader() {
        toggleLoader(View.VISIBLE);
        toggleDahsboard(View.GONE);
    }

    void toggleLoader(int visibility) {
        View layout = findViewById(R.id.lay_loader);
        layout.setVisibility(visibility);
    }

    void toggleDahsboard(int visibility) {
        View layout = findViewById(R.id.lay_dashboard);
        layout.setVisibility(visibility);
    }

    protected void showMonthsDialog() {
        AlertDialog dialog = null;
        AlertDialog.Builder builder;

        if (dialog == null) {
            builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.dlg_calendar_months);
            builder.setIcon(R.drawable.ic_calendar_24dp);

            builder.setSingleChoiceItems(
                    this.monthsAdapater,
                    this.monthsAdapater.getPosition(btnMonth.getText().toString()),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which > -1 && which < monthsAdapater.getCount()) {
                                btnMonth.setText(monthsAdapater.getItem(which));
                                selectedPeriode.set(Calendar.MONTH, which);
                                initServer();
                                showLoader();
                            }
                            dialog.dismiss();
                        }
                    });
            dialog = builder.create();
        }

        dialog.show();
    }

    protected void showYearDialog() {
        AlertDialog dialog = null;
        AlertDialog.Builder builder;

        if (dialog == null) {
            builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.dlg_calendar_years);
            builder.setIcon(R.drawable.ic_calendar_24dp);
            builder.setSingleChoiceItems(
                    this.yearAdapter,
                    this.yearAdapter.getPosition(btnYear.getText().toString()),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which > -1 && which < yearAdapter.getCount()) {
                                btnYear.setText(yearAdapter.getItem(which));
                                selectedPeriode.set(
                                        Calendar.YEAR,
                                        Integer.valueOf(yearAdapter.getItem(which))
                                );
                                initServer();
                                showLoader();
                            }
                            dialog.dismiss();
                        }
                    });
            builder.setPositiveButton(R.string.btn_ok, null);
            dialog = builder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    String selected = btnYear.getText().toString();
                    int index = yearAdapter.getPosition(selected);
                    if (index > -1 && index < yearAdapter.getCount()) {
                        ((AlertDialog) dialog).getListView().setSelection(index);
                    }
                }
            });
        }

        dialog.show();
    }

    /**
     * we create TableRow in this void, and pass the JSON "msg.records" values
     * to next void to create TableRow items
     * <p>
     * Signature of params
     * JSON
     * {
     * code:    int
     * msg:     {
     * "month":        String,
     * "year":         String,
     * "records":      {JSONObject}
     * }
     * }
     * <p>
     * Signature of JSON "records"
     * "records": {
     * "year":                 String,
     * "month":                String,
     * "day":                  String,
     * "dayOfWeek":            String,
     * "isHoliday":            true | false,
     * "hasAttendance":        true | false,
     * "hasConsent":           true | false,
     * "holiday":              String | false,
     * "attendance":           {JSONObject} if hasAttendance true | false
     * "consent":              {JSONObject} if hasConsent true | false
     * }
     *
     * @param params - the JSON object in form ServerResponseParamater
     */
    void fillTableLayout(ServerResponseParameters params) throws JSONException {
        ServerResponseParameters msg = params.getJSONParameters("msg");
        JSONObject recs = msg.getJSONObject("records");

        Iterator<String> keys = recs.keys();
        int index = 0;
        while (keys.hasNext()) {
            JSONObject currentRecord = recs.getJSONObject(keys.next());
            Log.d("jiwanala", "createRow: " + currentRecord);

            int padding = getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin_small);
            TableRow row = new TableRow(this);
            row.setOrientation(TableRow.VERTICAL);
            row.addView(createTableRowColumn_columnNumber(currentRecord));
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding(padding, 0, padding, 0);
            //if (index%2==0) {
            row.setBackgroundColor(Color.WHITE);
            //}

            if (currentRecord.getBoolean("isHoliday")) {
                row.addView(
                        createTableRowColumn_columnHoliday(currentRecord.getString("holiday")),
                        ViewUtils.createTableRowLayoutParams(false, false)
                );
                row.setBackgroundResource(R.color.table_row_holiday);
            } else if (currentRecord.getBoolean("hasAttendance")) {
                row.addView(
                        createTableRowColumn_columnFinger(currentRecord),
                        ViewUtils.createTableRowLayoutParams(false, false)
                );
                row.setGravity(Gravity.CENTER_VERTICAL);
            } else {

            }

            TableLayout.LayoutParams layoutParams = ViewUtils.createTableLayoutParams(true, false);
            layoutParams.setMargins(0, 0, 0, 2);
            tableAttendance.addView(row, layoutParams);
            tableAttendance.setBackgroundColor(Color.GRAY);
            index++;
        }
    }

    View createTableRowColumn_columnNumber(JSONObject data) throws JSONException {
        TextView tx = new TextView(this);
        int padding = getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin_small);

        tx.setText("01");
        tx.setPadding(padding, padding, padding, padding);
        tx.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tx.setTypeface(null, Typeface.BOLD);
        tx.setCompoundDrawablePadding(padding);

        if (data.getBoolean("isHoliday")) {
            Drawable icon = ViewUtils.getDrawable(this, R.drawable.flight_black_24dp);
            icon.setTint(Color.parseColor("#FEFEFE"));
            tx.setCompoundDrawables(null, null, null, icon);
            tx.setTextColor(Color.parseColor("#FEFEFE"));
        } else if (data.getBoolean("hasAttendance")) {
            tx.setCompoundDrawables(null, null, null, ViewUtils.getDrawable(this, R.drawable.fingerprint_black_24dp));
        } else {
        }

        try {
            tx.setText(data.getString("day"));
        } catch (JSONException ex) {
            tx.setText(ex.getMessage());
        }
        return tx;
    }

    View createTableRowColumn_columnFinger(JSONObject data) throws JSONException {
        /**
         *  <LinearLayout
         *      android:layout_height="wrap_content"
         *      android:layout_width="wrap_content"
         *      android:orientation="vertical">
         *
         *      <pre>
         *         createDataLayout(JSONObject)
         *      </pre>
         *
         *
         *  </LinearLayout>
         *  <LinearLayout
         *      android:layout_height="wrap_content"
         *      android:layout_width="wrap_content"
         *      android:orientation="vertical">
         *
         *      <pre>
         *         createDataLayout(JSONObject)
         *      </pre>
         *
         *  </LinearLayout>
         */
        JSONObject currentData = data.getJSONObject("attendance");
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        LinearLayout first = new LinearLayout(this);
        first.addView(
                createTableRowColumn_columnFinger_container(currentData),
                ViewUtils.createLinearLayoutParams(false, false)
        );
        root.addView(first, ViewUtils.createLinearLayoutParams(false, false));

        LinearLayout second = new LinearLayout(this);
        String summary = currentData.getString("message");
        if (summary.trim() != "null") {
            second.addView(
                    createTableRowColumn_columnFinger_containerSummary(summary),
                    ViewUtils.createLinearLayoutParams(false, false)
            );
            root.addView(second, ViewUtils.createLinearLayoutParams(false, false));
        }

        return root;
    }

    /**
     * JSON signature
     * <p>
     * "attendance":{
     * <p>
     * "id":       Integer,
     * "fin":      String | NULL,
     * "fout1":    String | NULL,
     * "fout2":    String | NULL,
     * "fout3":    String | NULL,
     * "message":  String | NULL
     * }
     *
     * @param data the json
     * @return View
     * @throws JSONException
     */
    View createTableRowColumn_columnFinger_container(JSONObject data) throws JSONException {
        /**
         *  <LinearLayout
         *      android:layout_height="wrap_content"
         *      android:layout_width="wrap_content"
         *      android:orientation="vertical">
         *
         *      <pre>
         *          this inner html filled with 2 line of
         *          createDataView(String, String)
         *      </pre>
         *
         *
         *  </LinearLayout>
         *  <LinearLayout
         *      android:layout_height="wrap_content"
         *      android:layout_width="wrap_content"
         *      android:orientation="vertical">
         *
         *      <pre>
         *          this inner html filled with 2 line of
         *          createDataView(String, String)
         *      </pre>
         *
         *  </LinearLayout>
         */
        LinearLayout first = new LinearLayout(this);
        LinearLayout second = new LinearLayout(this);
        for (int i = 0; i < 2; i++) {
            String labelFirst = i == 0 ? getString(R.string.lb_finger_in) + " :" : getString(R.string.lb_finger_out) + " 1 : ";
            String labelSecond = i == 0 ? getString(R.string.lb_finger_out) + " 2 : " : getString(R.string.lb_finger_out) + " 3 : ";
            String keyFirst = i == 0 ? "fin" : "fout1";
            String keySecond = i == 0 ? "fout2" : "fout3";
            String dataFirst = data.getString(keyFirst);
            String dataSecond = data.getString(keySecond);

            if (dataFirst.trim() != "null") {
                first.addView(
                        createTableRowColumn_columnFinger_containerData(labelFirst, dataFirst),
                        ViewUtils.createLinearLayoutParams(false, false)
                );
            }

            if (dataSecond.trim() != "null") {
                second.addView(
                        createTableRowColumn_columnFinger_containerData(labelSecond, dataSecond),
                        ViewUtils.createLinearLayoutParams(false, false)
                );
            }
        }

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.addView(first, ViewUtils.createLinearLayoutParams(false, false));
        root.addView(second, ViewUtils.createLinearLayoutParams(false, false));
        return root;
    }

    View createTableRowColumn_columnFinger_containerData(String labelText, String valueText) {
        /**
         *  <LinearLayout
         *      android:layout_width="wrap_content"
         *      android:layout_height="wrap_content"
         *      android:orientation="horizontal">
         *  <TextView android:layout_width="wrap_content"
         *      android:layout_height="wrap_content"
         *      android:paddingTop="@dimen/activity_horizontal_margin_small"
         *      android:paddingBottom="@dimen/activity_horizontal_margin_small"
         *      android:paddingLeft="@dimen/activity_horizontal_margin_small"
         *      android:width="70dp"
         *      android:text=""/>                       ---> labelText
         *  <TextView
         *      android:layout_width="wrap_content"
         *      android:layout_height="wrap_content"
         *      android:padding="@dimen/activity_horizontal_margin_small"
         *      android:text=""/>                       ---> valueText
         *  </LinearLayout>
         */
        int padding = getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin_small);
        int dp70 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());

        TextView label = new TextView(this);
        label.setWidth(dp70);
        label.setPadding(padding, padding, 0, padding);
        label.setText(labelText);

        TextView value = new TextView(this);
        value.setText(valueText);

        LinearLayout layout = new LinearLayout(this);

        layout.addView(label, ViewUtils.createLinearLayoutParams(false, false));
        layout.addView(value, ViewUtils.createLinearLayoutParams(false, false));
        return layout;
    }

    View createTableRowColumn_columnFinger_containerSummary(String summary) {
        int padding = getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin_small);
        TextView sum = new TextView(this);
        sum.setPadding(padding, 0, padding, padding);
        sum.setText(summary);
        sum.setTextColor(getResources().getColor(R.color.table_row_finger_summary));
        return sum;
    }

    View createTableRowColumn_columnHoliday(String holiday) {
        int padding = getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin_small);
        TextView view = new TextView(this);
        view.setText(holiday);
        view.setPadding(padding, padding, padding, padding);
        view.setTextColor(Color.parseColor("#FEFEFE"));
        return view;
    }
}