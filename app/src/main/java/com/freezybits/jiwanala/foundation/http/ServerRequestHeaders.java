package com.freezybits.jiwanala.foundation.http;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class ServerRequestHeaders {
    HashMap<String, String> properties;

    public ServerRequestHeaders() {
        this.properties = new HashMap<>();
    }

    public void addHeader(String key, String value) {
        this.properties.put(key, value);
    }

    public String getHeader(String key) {
        return this.properties.get(key);
    }

    public void write(
            HttpURLConnection connection,
            ServerRequestParameters parameters) throws UnsupportedEncodingException {

        connection.setRequestProperty("Content-Length",
                String.valueOf(
                        parameters.encodeRequestParameters().getBytes("UTF-8").length
                )
        );
        for (Map.Entry<String, String> entry : this.properties.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }
}
