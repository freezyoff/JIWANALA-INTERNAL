package com.freezybits.jiwanala.foundation.http;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class ServerConnection extends AsyncTask<ServerConnection, Integer, ServerResponse> {
    URL url;
    HttpsURLConnection connection;
    ServerRequestParameters parameters;
    ServerRequestHeaders headers;
    ServerResponse response;
    String method;

    public ServerConnection(URL url) {
        this.url = url;
        this.parameters = new ServerRequestParameters();
        this.headers = new ServerRequestHeaders();
        this.response = new ServerResponse();
    }

    public ServerConnection(String url) throws MalformedURLException {
        this(new URL(url));
    }

    public void setRequestMethod(String method) {
        this.method = method;
    }

    public HttpsURLConnection getConnection() throws IOException {
        if (this.connection == null) {
            this.connection = (HttpsURLConnection) this.url.openConnection();
        }
        return this.connection;
    }

    public ServerRequestParameters getRequestParameters() {
        return this.parameters;
    }

    public ServerRequestHeaders getRequestHeaders() {
        return this.headers;
    }

    public ServerResponse getServerResponse() {
        return this.response;
    }

    public void addServerResponseListener(ServerResponseListener listener) {
        this.getServerResponse().addListener(listener);
    }

    public void connect() {
        try {
            HttpsURLConnection connection = this.getConnection();
            connection.setRequestMethod(this.method);

            if (this.getRequestParameters().getParameters().size() > 0) {
                this.getRequestHeaders().write(connection, this.getRequestParameters());
                this.getRequestParameters().write(connection);
            }

            Log.d("jiwanala", "ServerConnection.connect() : " + this.url.toString());
            this.getServerResponse().read(connection);
            connection.disconnect();
            Log.d("jiwanala", "ServerConnection.disconnect()");
        } catch (IOException e) {
            Log.d("jiwanala", "ServerConnection error : " + connection.getErrorStream());
            e.printStackTrace();
        }
    }

    @Override
    protected ServerResponse doInBackground(ServerConnection... serverConnections) {
        this.connect();
        return this.getServerResponse();
    }

    @Override
    protected void onPostExecute(ServerResponse serverResponse) {
        serverResponse.fireListener();
    }
}
