package com.freezybits.jiwanala.foundation.http;

import org.json.JSONObject;

public interface ServerResponseListener {
    void serverResponseAccepted(String responseString, JSONObject responseJSON);
}
