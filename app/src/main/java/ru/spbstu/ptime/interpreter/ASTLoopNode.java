package ru.spbstu.ptime.interpreter;

public class ASTLoopNode extends ASTNode {
    private ASTNode inner;  // Поддерево внутри цикла
    private int iterations;
    public ASTLoopNode(ASTNode inner, int iterations) {
        this.inner = inner;
        this.iterations = iterations;
    }
    public ASTNode interpret(ASTInterpreter interpreter) {
        interpreter.runLoop(inner, iterations);
        return next;
    }
}
