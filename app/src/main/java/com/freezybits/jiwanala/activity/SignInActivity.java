package com.freezybits.jiwanala.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.freezybits.jiwanala.R;
import com.freezybits.jiwanala.foundation.SharedInstance;
import com.freezybits.jiwanala.foundation.http.ServerConnection;
import com.freezybits.jiwanala.foundation.http.ServerResponseListener;
import com.freezybits.jiwanala.foundation.http.ServerResponseParameters;
import com.freezybits.jiwanala.foundation.state.ClientSignInState;
import com.freezybits.jiwanala.foundation.state.ClientStateListener;
import com.freezybits.jiwanala.foundation.state.ClientStateManager;
import com.freezybits.jiwanala.utils.ViewUtils;

/**
 * A login screen that offers login via email/password.
 */
public class SignInActivity extends AppCompatActivity {

    private Button btn;
    private EditText txNIP;
    private EditText txPWD;
    private Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //if (this.getActionBar() != null) this.getActionBar().hide();
        //if (this.getSupportActionBar() != null) this.getSupportActionBar().hide();

        SharedInstance.install(this.getApplication());
        appContext = this;

        setContentView(R.layout.layout_login);

        showLoader();
        initLoginForm();
    }

    void showForm() {
        toggleLoader(View.GONE);
        toggleSignForm(View.VISIBLE);
    }

    void showLoader() {
        toggleLoader(View.VISIBLE);
        toggleSignForm(View.GONE);
    }

    void toggleLoader(int visibility) {
        View layout = findViewById(R.id.lay_loader);
        layout.setVisibility(visibility);
    }

    void toggleSignForm(int visibility) {
        View layout = findViewById(R.id.lay_login_form);
        layout.setVisibility(visibility);
    }

    protected void initLoginForm() {
        btn = findViewById(R.id.btnSignin);
        txNIP = findViewById(R.id.nip);
        txPWD = findViewById(R.id.pwd);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoader();
                ServerConnection con = SharedInstance.getServerManager().getSignInConnection(
                        txNIP.getText().toString(),
                        txPWD.getText().toString()
                );

                con.addServerResponseListener(new LoginResponseListener());
                con.execute();
                ViewUtils.hideKeyboard(SignInActivity.this);
            }
        });

        //check token expired or not. if expired show login form, else show dahsboard
        final ClientStateManager clientStateManager = SharedInstance.getClientStateManager();
        clientStateManager.getClientSignInState().addStateListener(new ClientStateListener() {
            @Override
            public void stateChange(Class cls, int oldState, int newState) {
                Log.d("jiwanala", "ClientSignInState.stateChange(): " + clientStateManager.getClientSignInState().isSignedIn());
                if (newState == ClientSignInState.SIGNED_IN) {
                    ((SignInActivity) appContext).showDashboardActivity();
                } else {
                    showForm();
                }
            }
        });
        clientStateManager.getClientSignInState().checkSignInToken();
    }

    protected void showDashboardActivity() {
        Intent intent = new Intent(this, AttendanceDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
        this.finish();
    }

    class LoginResponseListener implements ServerResponseListener {
        @Override
        public void serverResponseAccepted(int responseCode,
                                           ServerResponseParameters parameters) {
            String message = null;

            if (responseCode == 200) {
                String token = parameters.getJSONParameters("success").getString("token");
                if (token != null) {
                    SharedInstance.getClientStateManager()
                            .getClientSignInState().setState(ClientSignInState.SIGNED_IN);

                    boolean commit = SharedInstance.getDbManager().addPreference(
                            R.string.db_key_server_token,
                            token
                    );

                    /*
                    Log.d("jiwanala",
                            "DBManager.getPreference('"+
                                    getString(R.string.db_key_server_token)+"') - after login: "+
                                    dbManager.getPreference(R.string.db_key_server_token, ""));
                    */

                    //saved user token, else show error
                    if (commit) {
                        showDashboardActivity();
                        return;
                    }

                    //user token not saved in SharedPreference, we set the message null
                    message = getString(R.string.errors_login_client_error);
                }
            } else {
                if (parameters.getJSONParameters("error") != null) {
                    message = parameters.getJSONParameters("error").getString("msg");
                }
            }

            //token null, posibly parse json error. not expected JSON format.
            if (message == null) {
                message = getString(R.string.errors_login_server_error);
            }

            AlertDialog.Builder dialog = new AlertDialog.Builder(appContext);
            dialog.setCancelable(true);
            dialog.setTitle(R.string.title_error);
            dialog.setMessage(message);
            dialog.setPositiveButton(
                    R.string.btn_close,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = dialog.create();
            alert11.show();
            showForm();
        }
    }
}