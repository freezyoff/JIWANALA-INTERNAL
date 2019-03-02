package com.freezybits.jiwanala.foundation;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.view.View;

import com.freezybits.jiwanala.activity.SignInActivity;
import com.freezybits.jiwanala.foundation.http.ServerConnection;
import com.freezybits.jiwanala.foundation.http.ServerManager;
import com.freezybits.jiwanala.foundation.http.ServerResponseListener;
import com.freezybits.jiwanala.foundation.http.ServerResponseParameters;
import com.freezybits.jiwanala.foundation.state.ClientSignInState;
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

    public static View.OnClickListener getSingOutButtonListener(final Activity context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerConnection connection = SharedInstance.getServerManager().getSignOutConnection();
                connection.addServerResponseListener(new ServerResponseListener() {
                    @Override
                    public void serverResponseAccepted(int responseCode, ServerResponseParameters parameters) {
                        if (responseCode == 200) {
                            ClientStateManager manager = SharedInstance.getClientStateManager();
                            manager.getClientSignInState().setState(ClientSignInState.SINGED_OUT);

                            Intent intent = new Intent(context, SignInActivity.class);
                            context.startActivity(intent);
                            context.finish();
                        }
                    }
                });
                connection.execute();
            }
        };
    }
}
