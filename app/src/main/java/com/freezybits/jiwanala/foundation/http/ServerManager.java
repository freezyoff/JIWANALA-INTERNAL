package com.freezybits.jiwanala.foundation.http;

import android.content.Context;

import com.freezybits.jiwanala.R;

public class ServerManager {
    static final int url_login = R.string.server_url_login;

    public static ServerConnection getSignInConnection(Context context, String username, String password) {
        ServerConnection server = null;
        ServerRequestHeaders headers = null;
        ServerRequestParameters params = null;
        try {
            //ServerConnection server = new ServerConnection(context.getString(url_login));
            server = new ServerConnection(context.getString(url_login));
            server.setRequestMethod("POST");

            headers = server.getRequestHeaders();
            headers.addHeader("Content-Type", "application/x-www-form-urlencoded");
            headers.addHeader("Accept", "application/json");

            params = server.getRequestParameters();
            params.addParameter("name", username);
            params.addParameter("password", password);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return server;
    }
}
