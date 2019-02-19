package com.freezybits.jiwanala.foundation.http;

import android.content.Context;

import com.freezybits.jiwanala.R;

public class ServerManager {
    static final int url_login = R.string.server_url_login;

    public static String doLogin(Context context, String username, String password) {
        try {
            //ServerConnection server = new ServerConnection(context.getString(url_login));
            ServerConnection server = new ServerConnection(context.getString(url_login));
            server.setRequestMethod("POST");
            server.addPostData("name", username);
            server.addPostData("password", password);
            server.connect();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }

        return "berhasil";
    }
}
