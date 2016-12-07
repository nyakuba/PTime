package ru.spbstu.ptime.interpreter;

public class ASTTimerByIntervalNode extends ASTNode {
    protected long seconds;

    public ASTTimerByIntervalNode(long seconds) {
        this.seconds = seconds;
    }

    public long getSeconds() {
        return seconds;
    }

    @Override
    public ASTNode interpret(ASTInterpreter interpreter) {
        interpreter.runTimer(this);
        return next;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ASTTimerByIntervalNode) {
            return seconds == ((ASTTimerByIntervalNode) other).seconds;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) seconds;
    }
}
