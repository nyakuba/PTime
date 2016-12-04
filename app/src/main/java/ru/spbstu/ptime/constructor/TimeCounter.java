package ru.spbstu.ptime.constructor;


import android.os.Handler;

/**
 * Created by nick_yakuba on 12/4/16.
 */

public abstract class TimeCounter implements Runnable {
    private Object lock = new Object();
    private boolean mStarted = false;
    private boolean mPaused = false;
    private boolean mStopped = false;
    private long mStartTime;
    private long mCurrentTime;
    private long mEndTime;
    private long mTick;
    private Handler mHandler;
    private Runnable mUpdater = new Runnable() {
        @Override
        public void run() {
            onUpdateTime();
        }
    };

    public TimeCounter(long startTime, long endTime, long tick, Handler handler) {
        mStartTime = startTime;
        mCurrentTime = startTime;
        mEndTime = endTime;
        mTick = tick;
        mHandler = handler;
    }

    public long getStartTime() { return mStartTime; }
    public long getCurrentTime() { return mCurrentTime; }
    public long getEndTime() { return mEndTime; }
    public long getTick() { return mTick; }
    public boolean started() { return mStarted; }
    public boolean paused() { return mPaused; }
    public boolean stopped() { return mStopped; }

    @Override
    public void run() {
        synchronized (lock) {
            while (!mStarted) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onStarted();
            }
        });
        while (!mStopped && mCurrentTime < mEndTime) {
            synchronized (lock) {
                while (mPaused) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }
            }
            try {
                Thread.sleep(mTick);
            } catch (InterruptedException e) {
                // ignore
            }
            mCurrentTime += mTick;
            mHandler.post(mUpdater);
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onFinished();
            }
        });
        mStopped = true;
    }

    public void start() {
        synchronized (lock) {
            mStarted = true;
            mPaused = false;
            lock.notify();
        }
    }

    public void pause() {
        mPaused = true;
    }

    public void stop() {
        synchronized (lock) {
            mStarted = true;
            mPaused = false;
            mStopped = true;
            lock.notify();
            Thread.currentThread().interrupt();
        }
    }

    public void onStarted() {
        // do nothing by default
    }

    public void onFinished() {
        // do nothing by default
    }

    public abstract void onUpdateTime();

}
