package ru.spbstu.ptime.interpreter;

abstract public class ASTNode {
    private static final int hash = (int) (Math.random() * Long.MAX_VALUE);
    protected ASTNode next;

    public ASTNode() {
        this.next = null;
    }

    public void setNext(ASTNode next) {
        this.next = next;
    }

    abstract public ASTNode interpret(ASTInterpreter interpreter);

    @Override
    public boolean equals(Object other) {
        if (other instanceof ASTNode) {
            return next.equals(other);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (next != null) {
            return next.hashCode();
        }
        return hash;
    }
}
