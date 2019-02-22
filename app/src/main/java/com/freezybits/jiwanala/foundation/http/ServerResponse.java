package com.freezybits.jiwanala.foundation.http;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class ServerResponse {
    ArrayList<ServerResponseListener> listeners;
    String responseString;
    int responseCode;
    ServerResponseParameters serverResponseParameters;

    public ServerResponse() {
        this.listeners = new ArrayList<>();
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public String getResponseString() {
        return this.responseString;
    }

    public JSONObject getResponseJSON() {
        JSONObject json;
        try {
            json = new JSONObject(this.responseString);
        } catch (JSONException ex) {
            ex.printStackTrace();
            json = null;
        }
        return json;
    }

    public void read(HttpsURLConnection connection) throws IOException {

        this.responseCode = connection.getResponseCode();
        Log.d("jiwanala", "ServerConnection.connect() -> responseCode :" + this.responseCode);

        String line;
        this.responseString = "";
        InputStream istream = this.responseCode == 200 ? connection.getInputStream() : connection.getErrorStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(istream));
        while ((line = br.readLine()) != null) {
            this.responseString += line;
        }
        br.close();
        Log.d("jiwanala", "ServerConnection.readHttpResponse() -> response :" + this.responseString);
    }

    public void addListener(ServerResponseListener listener) {
        this.listeners.add(listener);
    }

    public void fireListener() {
        for (ServerResponseListener li : this.listeners) {
            li.serverResponseAccepted(
                    this.responseCode,
                    new ServerResponseParameters(this.getResponseString(), this.getResponseJSON())
            );
        }
    }

}
