package ru.spbstu.ptime.interpreter;

public class ASTStopwatchNode extends ASTNode {
    public ASTNode interpret(ASTInterpreter interpreter) {
        interpreter.runStopwatch();
        return next;
    }
}
