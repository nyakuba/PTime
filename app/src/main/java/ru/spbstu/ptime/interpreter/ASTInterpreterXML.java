package ru.spbstu.ptime.interpreter;

import java.util.Date;

public class ASTInterpreterXML implements ASTInterpreter {
    public void runTimer(Date date) {
        //...
    }
    public void runTimer(long seconds) {
        //...
    }
    public void runStopwatch() {
        //...
    }
    public void runLoop(ASTLoopNode node) {
        //...
    }
    public void run(Program prog) {
        //Компилируем дерево prog в xml-файл
        //...
    }
}