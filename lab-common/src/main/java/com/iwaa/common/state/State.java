package com.iwaa.common.state;

public final class State {

    private static boolean performanceStatus = true;

    private State() {
    }

    public static boolean getPerformanceStatus() {
        return performanceStatus;
    }

    public static void switchPerformanceStatus() {
        performanceStatus = !performanceStatus;
    }
}
