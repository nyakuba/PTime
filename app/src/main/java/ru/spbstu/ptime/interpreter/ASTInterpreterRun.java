package ru.spbstu.ptime.interpreter;

import java.util.Date;

public class ASTInterpreterRun implements ASTInterpreter {
    public void runTimer(Date date) {
        //...
    }
    public void runTimer(long seconds) {
        //...
    }
    public void runStopwatch() {
        //...
    }
    public void runLoop(ASTNode body, int iterations) {
        //...
    }
    public void run(ASTNode body) {
        //Проход по дереву
        //...
    }
    public void run(Program program) {
        //...
    }
}