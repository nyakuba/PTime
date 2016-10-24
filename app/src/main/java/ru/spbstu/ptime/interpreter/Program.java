package ru.spbstu.ptime.interpreter;

public class Program {
    private String name;
    private ASTNode root;
    public Program (String name, ASTNode root) {
        this.name = name;
        this.root = root;
    }
}