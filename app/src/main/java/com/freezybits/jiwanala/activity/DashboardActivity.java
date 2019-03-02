package com.freezybits.jiwanala.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.freezybits.jiwanala.R;
import com.freezybits.jiwanala.foundation.SharedInstance;
import com.freezybits.jiwanala.foundation.http.ServerConnection;
import com.freezybits.jiwanala.foundation.http.ServerResponseListener;
import com.freezybits.jiwanala.foundation.http.ServerResponseParameters;
import com.freezybits.jiwanala.foundation.state.ClientSignInState;
import com.freezybits.jiwanala.foundation.state.ClientStateManager;
import com.freezybits.jiwanala.utils.ViewUtils;

import org.json.JSONArray;
import org.json.JSONException;

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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

        //init topnav button signout
        ImageButton btn = findViewById(R.id.btn_signout);
        btn.setOnClickListener(SharedInstance.getSingOutButtonListener(this));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardActivity.this.showLoader();
                ServerConnection connection = SharedInstance.getServerManager().getSignOutConnection();
                connection.addServerResponseListener(new ServerResponseListener() {
                    @Override
                    public void serverResponseAccepted(int responseCode, ServerResponseParameters parameters) {
                        if (responseCode == 200) {
                            ClientStateManager manager = SharedInstance.getClientStateManager();
                            manager.getClientSignInState().setState(ClientSignInState.SINGED_OUT);

                            Intent intent = new Intent(DashboardActivity.this, SignInActivity.class);
                            DashboardActivity.this.startActivity(intent);
                            DashboardActivity.this.finish();
                        }
                    }
                });
                connection.execute();
            }
        });
        toggleDahsboard(View.VISIBLE);
    }

    void showDahboard() {
        toggleLoader(View.GONE);
        toggleDahsboard(View.VISIBLE);
    }

    void showLoader() {
        toggleLoader(View.VISIBLE);
        //toggleDahsboard(View.GONE);
    }

    void toggleLoader(int visibility) {
        View layout = findViewById(R.id.lay_loader);
        layout.setVisibility(visibility);
    }

    void toggleDahsboard(int visibility) {
        View layout = findViewById(R.id.lay_details);
        layout.setVisibility(visibility);
    }

    protected void showMonthsDialog() {
        AlertDialog dialog = null;
        AlertDialog.Builder builder;

        if (dialog == null) {
            builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.dlg_calendar_months);
            builder.setIcon(R.drawable.calendar_24dp_black);

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
            builder.setIcon(R.drawable.calendar_24dp_black);
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
     * "nip":       String,
     * "month":     String,
     * "year":      String,
     * "records":   {JSONObject}
     * }
     * }
     * <p>
     * Signature of JSON "records"
     * "records": {
     * "year":                  String,
     * "month":                 String,
     * "day":                   String,
     * "dayOfWeek":             String,
     * "isHoliday":             true | false,
     * "hasAttendance":         true | false,
     * "hasConsent":            true | false,
     * "hasWarning":            true | false,
     * "holiday":               String | false,
     * "attendance":            {JSONObject} if not hasAttendance null
     * "consent":               {JSONObject} if not hasConsent null
     * "warning":               {JSONObject} if not hasWarning null
     * }
     *
     * @param params - the JSON object in form ServerResponseParamater
     */
    void fillTableLayout(ServerResponseParameters params) throws JSONException {
        ServerResponseParameters msg = params.getJSONParameters("msg");
        ServerResponseParameters recs = msg.getJSONParameters("records");

        Iterator<String> keys = recs.getKeys();
        int index = 0;
        while (keys.hasNext()) {
            index++;
            ServerResponseParameters currentRecord = recs.getJSONParameters(keys.next());
            Log.d("jiwanala", "index: " + index + " createRow: " + currentRecord);


            TableRow.LayoutParams tbWrap = ViewUtils.createTableRowLayoutParams(false, false);
            TableRow row = createTableRow();

            String day = currentRecord.getString("day");
            day = day.length() < 2 ? "0" + day : day;
            LinearLayout number = createTableRowNumber(day, currentRecord.getString("dayOfWeek"));
            row.addView(number, tbWrap);

            if (currentRecord.has("isHoliday") && currentRecord.getBoolean("isHoliday")) {
                for (int i = 0; i < number.getChildCount(); i++) {
                    TextView numberDay = (TextView) number.getChildAt(i);
                    numberDay.setTextColor(getResources().getColor(R.color.table_row_holiday_text));
                }

                TableRow.LayoutParams trWrap1 = ViewUtils.createTableRowLayoutParams(false, false, 1.0f);
                trWrap1.gravity = Gravity.CENTER_VERTICAL;

                TextView holiday = createHolidayTableRow(currentRecord.getString("holiday"));
                holiday.setTextColor(getResources().getColor(R.color.table_row_holiday_text));

                row.addView(holiday, trWrap1);
                row.setBackgroundResource(R.color.table_row_holiday);
            } else {
                LinearLayout.LayoutParams mainParams = ViewUtils.createLinearLayoutParams(false, false);
                LinearLayout main = new LinearLayout(this);
                main.setOrientation(LinearLayout.VERTICAL);

                if (currentRecord.has("hasAttendance") && currentRecord.getBoolean("hasAttendance")) {
                    //  add attendance data
                    main.addView(createAttendanceTableRow(currentRecord.getJSONParameters("attendance")), mainParams);
                    //row.setGravity(Gravity.CENTER_VERTICAL);
                }

                if (currentRecord.has("hasWarning") && currentRecord.getBoolean("hasWarning")) {
                    LinearLayout warning = createAttendanceWarning(currentRecord.getJSONArray("warning"));
                    main.addView(warning, mainParams);
                }

                if (currentRecord.has("hasConsent") && currentRecord.getBoolean("hasConsent")) {
                    TextView text = createAttendanceConsent(currentRecord.getString("consent"));
                    main.addView(text, mainParams);
                    row.setBackgroundColor(getResources().getColor(R.color.table_row_consent));
                }

                TableRow.LayoutParams trWrap1 = ViewUtils.createTableRowLayoutParams(false, false, 1.0f);
                trWrap1.gravity = Gravity.CENTER_VERTICAL;
                row.addView(main, trWrap1);
            }

            TableLayout.LayoutParams layoutParams = ViewUtils.createTableLayoutParams(true, false);
            layoutParams.setMargins(0, 0, 0, 2);
            tableAttendance.addView(row, layoutParams);
            tableAttendance.setBackgroundColor(Color.GRAY);
        }
    }

    /**
     * <TableRow
     * android:layout_width="match_parent"
     * android:layout_height="wrap_content"
     * android:paddingLeft="8dp"
     * android:paddingRight="8dp"
     * android:paddingTop="8dp"
     * android:paddingBottom="8dp">
     */
    protected TableRow createTableRow() {
        int dp4 = ViewUtils.transDimension(TypedValue.COMPLEX_UNIT_DIP, 4);
        TableRow trow = new TableRow(this);
        trow.setBackgroundColor(Color.WHITE);
        trow.setPadding(0, dp4, 0, dp4);
        return trow;
    }

    /**
     * <TextView
     * android:text="01"
     * android:textSize="24sp"
     * android:gravity="center"
     * android:paddingRight="8dp"
     * />
     */
    protected LinearLayout createTableRowNumber(String day, String dayOfWeek) {
        int dp8 = ViewUtils.transDimension(TypedValue.COMPLEX_UNIT_DIP, 8);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp8, 0, 0, 0);

        LinearLayout.LayoutParams layoutParams = ViewUtils.createLinearLayoutParams(false, false);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        TextView tday = new TextView(this);
        tday.setText(day);
        tday.setTextSize(18);

        TextView tDayOfWeek = new TextView(this);
        tDayOfWeek.setText(dayOfWeek);

        layout.addView(tday, layoutParams);
        layout.addView(tDayOfWeek, layoutParams);
        return layout;
    }

    /**
     * Holiday or OffScheduleDay layout
     * <TableRow
     * android:layout_width="match_parent"
     * android:layout_height="wrap_content"
     * android:paddingLeft="16dp"
     * android:paddingRight="16dp"
     * android:paddingTop="8dp"
     * android:paddingBottom="8dp">
     * <p>
     * <!-- row number: already created -->
     * <TextView
     * android:layout_width="wrap_content"
     * android:layout_height="wrap_content"
     * android:text=""
     * android:textSize="24sp"
     * android:gravity="center"
     * android:paddingRight="8dp"
     * />
     * <p>
     * <!-- We create this -->
     * <TextView
     * android:layout_width="wrap_content"
     * android:layout_height="wrap_content"
     * android:text=""
     * android:layout_gravity="center_vertical"
     * android:paddingLeft="8dp"
     * android:layout_weight="1"
     * />
     * </TableRow>
     */
    protected TextView createHolidayTableRow(String holidayText) {
        int dp8 = ViewUtils.transDimension(TypedValue.COMPLEX_UNIT_DIP, 8);
        TextView tview = new TextView(this);
        tview.setText(holidayText);
        tview.setPadding(dp8, 0, 0, 0);
        return tview;
    }

    /**
     * <LinearLayout
     *      android:orientation="horizontal"
     *      android:layout_width="wrap_content"
     *      android:layout_height="wrap_content">
     *          <LinearLayout
     *              android:layout_weight="1"
     *              android:orientation="vertical"
     *              android:layout_width="wrap_content"
     *              android:layout_height="wrap_content">
     *              <TextView
     *                  android:layout_width="wrap_content"
     *                  android:layout_height="wrap_content"
     *                  android:text="Libur Semester"
     *                  android:layout_gravity="top"
     *                  android:paddingLeft="8dp"
     *                  android:paddingRight="8dp"
     *                  android:paddingBottom="8dp"
     *                  android:paddingTop="6dp" />
     *              <TextView
     *                  android:layout_width="wrap_content"
     *                  android:layout_height="wrap_content"
     *                  android:text="Libur Semester"
     *                  android:layout_gravity="top"
     *                  android:paddingLeft="8dp"
     *                  android:paddingRight="8dp"
     *                  android:paddingBottom="8dp"
     *                  android:paddingTop="6dp" />
     *          </LinearLayout>
     *          <LinearLayout
     *              android:orientation="vertical"
     *              android:layout_weight="1"
     *              android:layout_width="wrap_content"
     *              android:layout_height="wrap_content">
     *              <TextView
     *                  android:layout_width="wrap_content"
     *                  android:layout_height="wrap_content"
     *                  android:text="Libur Semester"
     *                  android:layout_gravity="top"
     *                  android:paddingLeft="8dp"
     *                  android:paddingRight="8dp"
     *                  android:paddingBottom="8dp"
     *                  android:paddingTop="6dp" />
     *              <TextView
     *                  android:layout_width="wrap_content"
     *                  android:layout_height="wrap_content"
     *                  android:text="Libur Semester"
     *                  android:layout_gravity="top"
     *                  android:paddingLeft="8dp"
     *                  android:paddingRight="8dp"
     *                  android:paddingBottom="8dp"
     *                  android:paddingTop="6dp" />
     *          </LinearLayout>
     *  </LinearLayout>
     *
     * @param data - json attendance data
     * @return View
     */
    protected LinearLayout createAttendanceTableRow(ServerResponseParameters data) {
        int dp8 = ViewUtils.transDimension(TypedValue.COMPLEX_UNIT_DIP, 8);
        int dp4 = ViewUtils.transDimension(TypedValue.COMPLEX_UNIT_DIP, 4);

        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.HORIZONTAL);

        TableRow.LayoutParams trWrap1 = ViewUtils.createTableRowLayoutParams(false, false, 1);
        LinearLayout.LayoutParams lnWrap = ViewUtils.createLinearLayoutParams(false, false);

        LinearLayout first = new LinearLayout(this);
        first.setOrientation(LinearLayout.VERTICAL);

        String text = data.getString("fin");
        if (data.has("fin") && text != "null") {
            TextView tview = new TextView(this);
            tview.setText(text == "null" ? "" : text);
            tview.setPadding(dp8, dp4, dp8, dp4);
            first.addView(tview, lnWrap);
        }

        text = data.getString("fout2");
        if (data.has("fout2") && text != "null") {
            TextView tview = new TextView(this);
            tview.setText(text == "null" ? "" : text);
            tview.setPadding(dp8, dp4, dp8, dp4);
            first.addView(tview, lnWrap);
        }

        LinearLayout second = new LinearLayout(this);
        second.setOrientation(LinearLayout.VERTICAL);

        text = data.getString("fout1");
        if (data.has("fout1") && text != "null") {
            TextView tview = new TextView(this);
            tview.setText(text == "null" ? "" : text);
            tview.setPadding(dp8, dp4, dp8, dp4);
            second.addView(tview, lnWrap);
        }

        text = data.getString("fout3");
        if (data.has("fout3") && text != "null") {
            TextView tview = new TextView(this);
            tview.setText(text == "null" ? "" : text);
            tview.setPadding(dp8, dp4, dp8, dp4);
            second.addView(tview, lnWrap);
        }

        main.addView(first, trWrap1);
        main.addView(second, trWrap1);
        return main;
    }


    /**
     *  <LinearLayout
     *      android:orientation="vertical"
     *      android:layout_width="wrap_content"
     *      android:layout_height="wrap_content">
     *      <TextView
     *          android:layout_width="wrap_content"
     *          android:layout_height="wrap_content"
     *          android:text="Warning warning warning"/>
     *      <TextView
     *          android:layout_width="wrap_content"
     *          android:layout_height="wrap_content"
     *          android:text="Warning warning warning"/>
     *  </LinearLayout>
     *
     * @param params
     * @return
     */
    protected LinearLayout createAttendanceWarning(JSONArray params) throws JSONException {
        LinearLayout.LayoutParams mainParams = ViewUtils.createLinearLayoutParams(false, false);

        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);

        int dp8 = ViewUtils.transDimension(TypedValue.COMPLEX_UNIT_DIP, 8);
        int dp2 = ViewUtils.transDimension(TypedValue.COMPLEX_UNIT_DIP, 2);

        mainParams.setMargins(dp8, dp2, dp8, dp2);

        int count = params.length();
        for (int i = 0; i < count; i++) {
            TextView text = new TextView(this);
            text.setText(params.getString(i));
            text.setBackgroundColor(getResources().getColor(R.color.table_row_warning));
            text.setTextColor(getResources().getColor(R.color.table_row_warning_text));
            text.setPadding(dp8, dp2, dp8, dp2);
            main.addView(text, mainParams);
        }

        return main;
    }

    protected TextView createAttendanceConsent(String text) {
        int dp8 = ViewUtils.transDimension(TypedValue.COMPLEX_UNIT_DIP, 8);
        TextView tview = new TextView(this);
        tview.setText(text);
        tview.setPadding(dp8, 0, 0, 0);
        return tview;
    }
}