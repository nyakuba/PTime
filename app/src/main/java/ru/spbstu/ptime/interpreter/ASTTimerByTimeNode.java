package ru.spbstu.ptime.interpreter;

import java.util.Date;

public class ASTTimerByTimeNode extends ASTNode {
    private Date date;
    public ASTTimerByTimeNode(Date date) {
        this.date = date;
    }
    public void interpret(ASTInterpreterRun interpreter) {
        interpreter.runTimer(this.date);
    }
}
