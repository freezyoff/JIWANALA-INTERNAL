package com.freezybits.jiwanala.foundation.http;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class ServerConnection extends AsyncTask<ServerConnection, Integer, String> {
    URL url;
    HttpsURLConnection httpConnection;
    HashMap<String, String> httpPostData;
    String httpResponseString;

    public ServerConnection(URL url) {
        this.url = url;
        this.httpPostData = new HashMap<String, String>();
    }

    public ServerConnection(String url) throws MalformedURLException {
        this(new URL(url));
    }

    public String getHttpResponseString() {
        return this.httpResponseString;
    }

    protected void setHttpResponseString(String value) {
        this.httpResponseString = value;
    }

    public HttpsURLConnection getConnection() throws IOException {
        if (httpConnection == null) {
            this.httpConnection = (HttpsURLConnection) url.openConnection();
            this.httpConnection.setReadTimeout(3000);
            this.httpConnection.setConnectTimeout(3000);
            this.httpConnection.setDoInput(true);
            this.httpConnection.setDoOutput(true);
        }
        return this.httpConnection;
    }

    public void setRequestMethod(String method) throws IOException {
        this.getConnection().setRequestMethod(method);
    }

    public void addPostData(String key, String value) {
        this.httpPostData.put(key, value);
    }

    public HashMap<String, String> getPostData() {
        return this.httpPostData;
    }

    public String getPostDataString() throws UnsupportedEncodingException {
        StringBuilder postData = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> param : this.getPostData().entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        return postData.toString();
    }

    void writeHttpData(HttpsURLConnection connection) throws IOException {
        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
        writer.write(this.getPostDataString());
        writer.flush();
        writer.close();
        os.close();
    }

    public String readHttpResponse(HttpsURLConnection con) throws IOException {
        String line;
        String response = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        while ((line = br.readLine()) != null) {
            response += line;
        }
        br.close();

        return response;
    }

    public void connect() {
        this.execute(this);
    }

    protected void _connect() {
        try {
            if (this.getPostData().size() > 0) {
                this.getConnection().setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                this.getConnection().setRequestProperty("Content-Length",
                        String.valueOf(
                                this.getPostDataString().getBytes("UTF-8").length
                        )
                );
                this.writeHttpData(this.getConnection());
            }
            int responseCode = this.getConnection().getResponseCode();
            Log.d("jiwanala", "ServerConnection.connect() -> responseCode :" + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String response = this.readHttpResponse(this.getConnection());
                Log.d("jiwanala", "ServerConnection.readHttpResponse() -> response :" + response);
                this.setHttpResponseString(response);
            }
            Log.d("jiwanala", "ServerConnection.disconnect()");
            this.getConnection().disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getHttpResponse() {
        try {
            return new JSONObject(this.getHttpResponseString());
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    protected String doInBackground(ServerConnection... serverConnections) {
        serverConnections[0]._connect();
        return serverConnections[0].getHttpResponseString();
    }
}
