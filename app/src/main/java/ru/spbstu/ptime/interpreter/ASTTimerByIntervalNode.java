package ru.spbstu.ptime.interpreter;

public class ASTTimerByIntervalNode extends ASTNode {
    private long seconds;
    public ASTTimerByIntervalNode(long seconds) {
        this.seconds = seconds;
    }
    public void interpret(ASTInterpreterRun interpreter) {
        interpreter.runTimer(this.seconds);
    }
}
