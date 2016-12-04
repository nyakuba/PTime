package ru.spbstu.ptime.constructor;

/**
 * Created by nick_yakuba on 12/4/16.
 */

public class TimeController {
    TimeCounter mCounter = null;

    public boolean running() {
        return mCounter != null && !mCounter.stopped();
    }

    public void setTimeCounter(TimeCounter counter) {
        if (mCounter != null) {
            mCounter.stop();
        }
        mCounter = counter;
    }

    public void start() {
        if (mCounter != null) {
            mCounter.start();
        }
    }

    public void pause() {
        if (mCounter != null) {
            mCounter.pause();
        }
    }

    public void stop() {
        if (mCounter != null) {
            mCounter.stop();
            mCounter = null;
        }
    }
}
