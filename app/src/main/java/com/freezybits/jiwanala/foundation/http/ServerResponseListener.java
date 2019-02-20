package com.freezybits.jiwanala.foundation.http;

public interface ServerResponseListener {
    void serverResponseAccepted(int responseCode, ServerResponseParameters parameters);
}
