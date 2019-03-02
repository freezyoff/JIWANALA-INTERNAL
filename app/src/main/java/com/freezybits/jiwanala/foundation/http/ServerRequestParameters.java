package com.freezybits.jiwanala.foundation.http;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class ServerRequestParameters {
    HashMap<String, String> parameters;

    public ServerRequestParameters() {
        this.parameters = new HashMap<>();
    }

    public void addParameter(String key, String value) {
        this.parameters.put(key, value);
    }

    public HashMap<String, String> getParameters() {
        return this.parameters;
    }

    public String encodeRequestParameters() throws UnsupportedEncodingException {
        StringBuilder postData = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> param : this.getParameters().entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        return postData.toString();
    }

    public void write(HttpURLConnection connection) throws IOException {
        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
        writer.write(this.encodeRequestParameters());
        writer.flush();
        writer.close();
        os.close();
    }
}
