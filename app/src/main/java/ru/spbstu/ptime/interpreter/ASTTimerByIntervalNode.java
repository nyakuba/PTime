package ru.spbstu.ptime.interpreter;

public class ASTTimerByIntervalNode extends ASTNode {
    private long seconds;
    public ASTTimerByIntervalNode(long seconds) {
        this.seconds = seconds;
    }
    public ASTNode interpret(ASTInterpreter interpreter) {
        interpreter.runTimer(this.seconds);
        return next;
    }
}
