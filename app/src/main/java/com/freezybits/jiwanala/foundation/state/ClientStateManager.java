package com.freezybits.jiwanala.foundation.state;

import com.freezybits.jiwanala.foundation.storage.DBManager;

public class ClientStateManager {
    private static ClientStateManager manager;

    private DBManager dbManager;
    private ClientLoginState clientLoginState;

    public ClientStateManager(DBManager dbManager) {
        this.dbManager = dbManager;
        this.clientLoginState = new ClientLoginState(this.dbManager);
    }

    public static ClientStateManager getInstance(DBManager dbManager) {
        if (ClientStateManager.manager == null)
            ClientStateManager.manager = new ClientStateManager(dbManager);
        return ClientStateManager.manager;
    }

    public ClientLoginState getClientLoginState() {
        return this.clientLoginState;
    }
}
