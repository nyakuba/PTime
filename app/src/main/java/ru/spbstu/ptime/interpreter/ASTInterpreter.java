package ru.spbstu.ptime.interpreter;

import java.util.Date;

public interface ASTInterpreter {
    void runTimer(Date date);
    void runTimer(long seconds);
    void runStopwatch();
    void runLoop(ASTLoopNode node);
    void run(Program program);
}