package ru.spbstu.ptime.interpreter;

import java.util.Date;

public interface ASTInterpreter {
    void runTimer(Date date);
    void runTimer(long seconds);
    void runStopwatch();
    void runLoop(ASTNode body, int iterations);
    void run(ASTNode body);
    void run(Program program);
}