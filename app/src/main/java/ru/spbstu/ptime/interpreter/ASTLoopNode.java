package ru.spbstu.ptime.interpreter;

public class ASTLoopNode extends ASTNode {
    protected ASTNode inner;  // Поддерево внутри цикла
    protected int iterations;

    public ASTLoopNode(ASTNode inner, int iterations) {
        this.inner = inner;
        this.iterations = iterations;
    }

    public void setBody(ASTNode body) {
        this.inner = body;
    }
    public ASTNode getBody() {
        return inner;
    }
    public int getIterations() {
        return iterations;
    }

    @Override
    public ASTNode interpret(ASTInterpreter interpreter) {
        interpreter.runLoop(this);
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
