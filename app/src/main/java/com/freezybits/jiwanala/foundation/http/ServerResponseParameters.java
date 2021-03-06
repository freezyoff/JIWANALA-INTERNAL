package com.freezybits.jiwanala.foundation.http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class ServerResponseParameters {

    protected JSONObject json;

    protected ServerResponseParameters(String string, JSONObject json) {
        this.json = json;
    }

    public boolean has(String key) {
        return json.has(key);
    }

    public Iterator<String> getKeys() {
        return json.keys();
    }

    public String getString(String key) {
        try {

            return json.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer getInt(String key) {
        try {
            return json.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean getBoolean(String key) {
        try {
            return json.getBoolean(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Long getLong(String key) {
        try {
            return json.getLong(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Double getDouble(String key) {
        try {
            return json.getDouble(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONArray getJSONArray(String key) {
        try {
            return json.getJSONArray(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getJSONObject(String key) {
        try {
            return json.getJSONObject(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ServerResponseParameters getJSONParameters(String key) {
        try {
            if (json != null) {
                JSONObject obj = json.getJSONObject(key);
                return new ServerResponseParameters(obj.toString(), obj);
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object getObject(String key) {
        try {
            return json.get(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "ServerResponseParameters{" +
                "json=" + json.toString() +
                '}';
    }
}
