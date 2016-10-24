package ru.spbstu.ptime.interpreter;

public class ASTStopwatchNode extends ASTNode {
    public void interpret(ASTInterpreterRun interpreter) {
        interpreter.runStopwatch();
    }
}
