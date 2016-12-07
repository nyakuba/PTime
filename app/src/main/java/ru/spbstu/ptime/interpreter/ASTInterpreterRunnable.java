package ru.spbstu.ptime.interpreter;

import ru.spbstu.ptime.constructor.TimeController;
import ru.spbstu.ptime.constructor.TimeEngine;
import ru.spbstu.ptime.constructor.ViewUpdater;

public class ASTInterpreterRunnable implements ASTInterpreter, Runnable {
    private Program program;
    private final Object lock = new Object();
    private ViewUpdater<Long> viewUpdater;
    public ASTInterpreterRunnable(Program program) {
        this.program = program;
    }
    public void setViewUpdater(ViewUpdater<Long> viewUpdater) {
        this.viewUpdater = viewUpdater;
    }
    public void runTimer(ASTTimerByTimeNode timerNode) {
        // ...
    }
    public void runTimer(ASTTimerByIntervalNode timerItem) {
        synchronized (lock) {
            final TimeController timer = new TimeController();
            TimeEngine.startIntervalTimer(timer, viewUpdater, timerItem.getSeconds(), this);
            timer.start();
            try {
                lock.wait();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void runStopwatch(ASTStopwatchNode stopwatchNode) {
        synchronized (lock) {
            final TimeController stopwatch = new TimeController();
            TimeEngine.startStopwatch(stopwatch, viewUpdater, this);
            stopwatch.start();
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void stopTimeProcess() {
        synchronized (lock) {
            lock.notify();
        }
    }
    public void runLoop(ASTLoopNode loopNode) {
        int i = loopNode.getIterations();
        while (0 != i--)
            run(loopNode.getBody());
    }
    public void run(ASTNode body) {
        ASTNode curr = body;
        while (null != curr)
            curr = curr.interpret(this);
    }
    public void run(Program program) {
        run(program.getBody());
    }
    public void run() {
        run(program);
    }
}