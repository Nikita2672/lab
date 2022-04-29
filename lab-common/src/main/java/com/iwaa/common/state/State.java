package com.iwaa.common.state;

public final class State {

    private boolean performanceStatus = true;

    public State() {
    }

    public boolean getPerformanceStatus() {
        return performanceStatus;
    }

    public void switchPerformanceStatus() {
        performanceStatus = !performanceStatus;
    }
}
