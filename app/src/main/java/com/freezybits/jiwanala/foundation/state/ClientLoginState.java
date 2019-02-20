package com.freezybits.jiwanala.foundation.state;

import com.freezybits.jiwanala.R;
import com.freezybits.jiwanala.foundation.storage.DBManager;

public class ClientLoginState extends ClientState {
    public static final int SIGNED_IN = 0x01;
    public static final int SINGED_OUT = 0x00;
    private static ClientLoginState instance;
    protected DBManager dbManager;

    ClientLoginState(DBManager manager) {
        super(SINGED_OUT);
        this.dbManager = manager;
        String serverToken = dbManager.getPreference(R.string.db_key_server_token, "");
        if (serverToken != "") {
            this.setState(SIGNED_IN);
        }
    }

    public static ClientLoginState getInstance(DBManager manager) {
        if (instance == null) instance = new ClientLoginState(manager);
        return instance;
    }

    public boolean isSignedIn() {
        return this.isState(SIGNED_IN);
    }

    public boolean isSignedOut() {
        return this.isState(SINGED_OUT);
    }
}
