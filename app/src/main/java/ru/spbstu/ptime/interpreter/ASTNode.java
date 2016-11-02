package ru.spbstu.ptime.interpreter;

abstract public class ASTNode {
    protected ASTNode next;
    public ASTNode()
    {
        this.next = null;
    }
    public void setNext(ASTNode next) {
        this.next = next;
    }
    abstract public ASTNode interpret(ASTInterpreter interpreter);
}
