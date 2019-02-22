package com.freezybits.jiwanala.foundation.state;

import com.freezybits.jiwanala.foundation.storage.DBManager;

public class ClientStateManager {
    private static ClientStateManager manager;

    private DBManager dbManager;
    private ClientSignInState clientSignInState;

    public ClientStateManager(DBManager dbManager) {
        this.dbManager = dbManager;
        this.clientSignInState = ClientSignInState.getInstance(this.dbManager);
    }

    public static ClientStateManager getInstance(DBManager dbManager) {
        if (ClientStateManager.manager == null)
            ClientStateManager.manager = new ClientStateManager(dbManager);
        return ClientStateManager.manager;
    }

    public ClientSignInState getClientSignInState() {
        return this.clientSignInState;
    }
}
