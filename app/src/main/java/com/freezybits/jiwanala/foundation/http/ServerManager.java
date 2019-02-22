package com.freezybits.jiwanala.foundation.http;

import android.app.Application;

import com.freezybits.jiwanala.R;
import com.freezybits.jiwanala.foundation.SharedInstance;
import com.freezybits.jiwanala.foundation.storage.DBManager;

public class ServerManager {
    Application application;

    public ServerManager(Application app) {
        application = app;
    }

    public String getServerToken() {
        DBManager dbManager = SharedInstance.getDbManager();
        return dbManager.getPreference(R.string.db_key_server_token, "");
    }

    public ServerConnection getSignInConnection(String username, String password) {
        ServerConnection server = null;
        ServerRequestHeaders headers = null;
        ServerRequestParameters params = null;
        try {
            server = new ServerConnection(application.getString(R.string.server_url_login));
            server.setRequestMethod("POST");

            headers = server.getRequestHeaders();
            headers.addHeader("Accept", "application/json");
            headers.addHeader("Content-Type", "application/x-www-form-urlencoded");

            params = server.getRequestParameters();
            params.addParameter("name", username);
            params.addParameter("password", password);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return server;
    }

    public ServerConnection getCheckTokenConnection() {
        ServerConnection server = null;
        ServerRequestHeaders headers = null;
        ServerRequestParameters params = null;
        try {
            server = new ServerConnection(application.getString(R.string.server_url_relogin));
            server.setRequestMethod("POST");

            headers = server.getRequestHeaders();
            headers.addHeader("Accept", "application/json");
            headers.addHeader("Authorization", getServerToken());

            headers.addHeader("Content-Type", "application/x-www-form-urlencoded");
            params = server.getRequestParameters();
            params.addParameter("token", getServerToken());

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return server;
    }

    public ServerConnection getAttendanceHistoryConnection(int month, int year) {
        ServerConnection server = null;
        ServerRequestHeaders headers = null;
        ServerRequestParameters params = null;
        try {
            server = new ServerConnection(application.getString(R.string.server_url_attendance_histories));
            server.setRequestMethod("POST");

            headers = server.getRequestHeaders();
            headers.addHeader("Accept", "application/json");
            headers.addHeader("Authorization", getServerToken());

            headers.addHeader("Content-Type", "application/x-www-form-urlencoded");
            params = server.getRequestParameters();
            params.addParameter("month", String.valueOf(month));
            params.addParameter("year", String.valueOf(year));

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return server;
    }
}
