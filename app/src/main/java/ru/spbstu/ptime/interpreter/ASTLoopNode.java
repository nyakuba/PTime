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

    @Override
    public boolean equals(Object other) {
        if (other instanceof ASTLoopNode) {
            ASTLoopNode node = (ASTLoopNode) other;
            return inner.equals(node.inner) && iterations == node.iterations;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return inner.hashCode()*31 + iterations;
    }
}
