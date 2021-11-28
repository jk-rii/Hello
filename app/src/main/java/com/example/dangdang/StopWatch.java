package com.example.dangdang;

public class StopWatch {
    private long startTime = 0;
    private long stopTime = 0;
    private boolean isRunning = false;

    public void start() {
        //스탑 워치를 시작하는함수.
        if (startTime == 0) startTime = System.currentTimeMillis();
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }

    public long getElapsedTime() {
        long diff;
        if (isRunning) {
            diff = (System.currentTimeMillis() - startTime);
        } else {
            diff = stopTime - startTime;
        }
        return diff;
    }
}
