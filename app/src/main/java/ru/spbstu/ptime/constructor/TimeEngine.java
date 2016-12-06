package ru.spbstu.ptime.constructor;

import android.os.Handler;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ru.spbstu.ptime.interpreter.ASTInterpreter;
import ru.spbstu.ptime.interpreter.ASTInterpreterUI;

public class TimeEngine {
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    // Sets the amount of time an idle thread waits before terminating
    private static final int KEEP_ALIVE_TIME = 1;
    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private static final LinkedBlockingQueue<Runnable> mWorkQueue = new LinkedBlockingQueue<>();
    private static final ThreadPoolExecutor mThreadPool = new ThreadPoolExecutor(
            NUMBER_OF_CORES,       // Initial pool size
            NUMBER_OF_CORES,       // Max pool size
            KEEP_ALIVE_TIME,
            KEEP_ALIVE_TIME_UNIT,
            mWorkQueue);
    private static final Handler mHandler = new Handler();

    // no instances of TimeEngine is allowed
    private TimeEngine() { }

    public static void startIntervalTimer(final TimeController controller, final ViewUpdater<Long> updater, long seconds, ASTInterpreterUI interpreter) {
        long currentTime = System.currentTimeMillis();
        TimeCounter counter = new TimeCounter(currentTime, currentTime + seconds*1000, 1000, mHandler) {
            @Override
            public void onStarted() {
                updater.updateView(Math.max(0, seconds - 1));
            }

            @Override
            public void onUpdateTime() {
                updater.updateView((getEndTime() - getCurrentTime()) / getTick());
            }

            @Override
            public void onFinished() {
                updater.updateView(0L);
                interpreter.stopTimeProcess();
            }
        };
        controller.setTimeCounter(counter);
        mThreadPool.execute(counter);
    }

    public static void startStopwatch(final TimeController controller, final ViewUpdater<Long> updater, ASTInterpreterUI interpreter) {
        long currentTime = System.currentTimeMillis();
        TimeCounter counter = new TimeCounter(currentTime, 30, mHandler) {
            @Override
            public void onUpdateTime() {
                updater.updateView(getCurrentTime() - getStartTime());
            }

            @Override
            public void onFinished() {
                updater.updateView(0L);
                interpreter.stopTimeProcess();
            }
        };
        controller.setTimeCounter(counter);
        mThreadPool.execute(counter);
    }


    public void close() {
        mThreadPool.shutdownNow();
    }
}

