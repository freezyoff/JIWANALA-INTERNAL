package com.freezybits.jiwanala.foundation.state;

import com.freezybits.jiwanala.R;
import com.freezybits.jiwanala.foundation.SharedInstance;
import com.freezybits.jiwanala.foundation.http.ServerConnection;
import com.freezybits.jiwanala.foundation.http.ServerResponseListener;
import com.freezybits.jiwanala.foundation.http.ServerResponseParameters;
import com.freezybits.jiwanala.foundation.storage.DBManager;

public class ClientSignInState extends ClientState {
    public static final int SIGNED_IN = 0x01;
    public static final int SINGED_OUT = 0x00;
    public static final int EXPIRED_TOKEN = 0x02;

    private static ClientSignInState instance;
    protected DBManager dbManager;

    ClientSignInState(DBManager manager) {
        super(SINGED_OUT);
        this.dbManager = manager;
    }

    public static ClientSignInState getInstance(DBManager manager) {
        if (instance == null) instance = new ClientSignInState(manager);
        return instance;
    }

    /**
     * check given token match with server token.
     * it will set the current state and fire the listener
     */
    public void checkSignInToken() {
        //retrive server token from sharedPreference
        String token = SharedInstance.getServerManager().getServerToken();

        if (token != "") {
            ServerConnection connection = SharedInstance.getServerManager().getCheckTokenConnection();
            connection.addServerResponseListener(new ServerResponseListener() {
                @Override
                public void serverResponseAccepted(int responseCode, ServerResponseParameters parameters) {
                    if (responseCode == 200) {
                        boolean signedIn = parameters.getJSONParameters("success").getInt("code") == 200;
                        getInstance(dbManager).setState(signedIn ? SIGNED_IN : SINGED_OUT);
                    } else {
                        //remove expired token
                        dbManager.addPreference(R.string.db_key_server_token, "");
                        getInstance(dbManager).setState(EXPIRED_TOKEN);
                    }
                }
            });
            connection.execute();
        } else {
            this.setState(SINGED_OUT);
        }
    }

    public boolean isSignedIn() {
        return this.isState(SIGNED_IN);
    }

    public boolean isSignedOut() {
        return this.isState(SINGED_OUT);
    }
}
