package ru.spbstu.ptime.interpreter;

public class Program {
    private String name;
    private ASTNode root;

    public Program (String name, ASTNode root) {
        this.name = name;
        this.root = root;
    }

    public String getName() {
        return name;
    }

    public ASTNode getBody() {
        return root;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Program) {
            Program program = (Program) other;
            return name.equals(program.name) && root.equals(program.root);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode()*31 + root.hashCode();
    }
}