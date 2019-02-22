package com.freezybits.jiwanala.foundation;

import android.app.Application;

import com.freezybits.jiwanala.foundation.http.ServerManager;
import com.freezybits.jiwanala.foundation.state.ClientStateManager;
import com.freezybits.jiwanala.foundation.storage.DBManager;

public class SharedInstance {
    static Application application;
    static DBManager dbManager;
    static ServerManager serverManager;
    static ClientStateManager clientStateManager;

    public static void install(Application app) {
        if (dbManager == null) dbManager = new DBManager(app);
        if (clientStateManager == null) clientStateManager = new ClientStateManager(dbManager);
        if (serverManager == null) serverManager = new ServerManager(app);
        if (serverManager == null) application = app;
    }

    public static Application getApplication() {
        return application;
    }

    public static DBManager getDbManager() {
        return dbManager;
    }

    public static ServerManager getServerManager() {
        return serverManager;
    }

    public static ClientStateManager getClientStateManager() {
        return clientStateManager;
    }
}
