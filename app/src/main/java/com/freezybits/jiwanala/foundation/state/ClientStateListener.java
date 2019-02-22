package com.freezybits.jiwanala.foundation.state;

public interface ClientStateListener {
    /**
     * listener state change
     *
     * @param cls
     * @param oldState
     * @param newState
     */
    void stateChange(Class cls, int oldState, int newState);
}
