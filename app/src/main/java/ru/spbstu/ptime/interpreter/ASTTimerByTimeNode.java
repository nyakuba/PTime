package ru.spbstu.ptime.interpreter;

import java.util.Date;

public class ASTTimerByTimeNode extends ASTNode {
    protected Date date;

    public ASTTimerByTimeNode(Date date) {
        this.date = date;
    }

    @Override
    public ASTNode interpret(ASTInterpreter interpreter) {
        interpreter.runTimer(this.date);
        return next;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ASTTimerByTimeNode) {
            return date.equals(((ASTTimerByTimeNode) other).date);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }
}
