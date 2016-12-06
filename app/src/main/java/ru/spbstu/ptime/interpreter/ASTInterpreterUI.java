package ru.spbstu.ptime.interpreter;

import java.util.Date;

public class ASTInterpreterUI implements ASTInterpreter, Runnable {
    private Program program;
    private final Object lock = new Object();
    ASTInterpreterUI(Program program) {
        this.program = program;
    }
    public void runTimer(Date date) {
        // ...
    }
    public void runTimer(long seconds) {
        synchronized (lock) {
            try {
                lock.wait();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void runStopwatch() {
        synchronized (lock) {
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
    public void runLoop(ASTNode body, int iterations) {
        while (0 != iterations--)
            run(body);
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