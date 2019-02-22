package com.freezybits.jiwanala.foundation.state;

import java.util.ArrayList;
import java.util.List;

public abstract class ClientState {

    protected int currentState;
    protected List<ClientStateListener> clientStateListenerList;

    protected ClientState() {
        this(0);
    }

    protected ClientState(int defaultState) {
        this.currentState = defaultState;
        this.clientStateListenerList = new ArrayList<>();
    }

    public int getState() {
        return currentState;
    }

    public void setState(int state) {
        int oldState = this.getState();
        this.currentState = state;
        this.fireStateChange(oldState, currentState);
    }

    /**
     * check if current sate equal given sate
     *
     * @param state
     */
    public boolean isState(int state) {
        return this.currentState == state;
    }

    public void addStateListener(ClientStateListener listener) {
        this.clientStateListenerList.add(listener);
    }

    public void fireStateChange(int oldState, int newState) {
        if (this.clientStateListenerList == null) return;

        for (ClientStateListener sl : this.clientStateListenerList) {
            sl.stateChange(sl.getClass(), oldState, newState);
        }
    }
}
