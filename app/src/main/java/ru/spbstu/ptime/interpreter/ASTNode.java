package ru.spbstu.ptime.interpreter;

abstract public class ASTNode {
    protected ASTNode next;
    public void setNext(ASTNode next) {
        this.next = next;
    }
    abstract public ASTNode interpret(ASTInterpreterRun interpreter); // Returns next node to interpret
}
