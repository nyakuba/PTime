package ru.spbstu.ptime.interpreter;

public interface ASTBuilder<T> {
    Program parse(T input);
}