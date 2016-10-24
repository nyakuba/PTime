package ru.spbstu.ptime.interpreter;

abstract public class ASTNode {
    protected ASTNode next;
    public void setNext(ASTNode next) {
        this.next = next;
    }
    abstract public void interpret(ASTInterpreterRun interpreter);
}
