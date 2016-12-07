package ru.spbstu.ptime.interpreter;

public class ASTStopwatchNode extends ASTNode {
    private static final int hash = (int) (Math.random() * Long.MAX_VALUE);

    @Override
    public ASTNode interpret(ASTInterpreter interpreter) {
        interpreter.runStopwatch(this);
        return next;
    }

    @Override
    public boolean equals(Object other) {
        return true;
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
