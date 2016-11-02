package ru.spbstu.ptime.interpreter;

import java.util.Date;

public class ASTTimerByTimeNode extends ASTNode {
    private Date date;
    public ASTTimerByTimeNode(Date date) {
        this.date = date;
    }
    public ASTNode interpret(ASTInterpreter interpreter) {
        interpreter.runTimer(this.date);
        return next;
    }
}
