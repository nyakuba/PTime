package ru.spbstu.ptime.interpreter;

import java.util.Date;

public interface ASTInterpreter {
    void runTimer(ASTTimerByTimeNode timerNode);
    void runTimer(ASTTimerByIntervalNode timerNode);
    void runStopwatch(ASTStopwatchNode stopwatchNode);
//    void stopTimeProcess();
    void runLoop(ASTLoopNode loopNode);
    void run(ASTNode body);
    void run(Program program);
}