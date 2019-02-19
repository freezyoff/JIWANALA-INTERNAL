package com.freezybits.jiwanala.foundation.state;

public interface ClientStateListener {
    void stateChange(Class cls, int oldState, int newState);

    /**
     * Test if this state is equal to given state
     *
     * @param state integer state
     * @return True if equal, false other wise
     */
    boolean isState(int state);
}
