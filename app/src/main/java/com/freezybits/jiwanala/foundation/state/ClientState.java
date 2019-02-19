package com.freezybits.jiwanala.foundation.state;

import java.util.List;

public abstract class ClientState {

    protected int currentState;
    protected List<ClientStateListener> clientStateListenerList;

    protected ClientState(int defaultState) {
        this.currentState = defaultState;
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setState(int state) {
        int oldState = this.getCurrentState();
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
        for (ClientStateListener sl : this.clientStateListenerList) {
            sl.stateChange(sl.getClass(), oldState, newState);
        }
    }
}
