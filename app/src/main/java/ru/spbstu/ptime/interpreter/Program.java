package ru.spbstu.ptime.interpreter;

abstract class ASTNode {
    ASTNode left, right;
    abstract ASTNode interpret(PTInterpreter interpreter); // Returns next node to interpret
}

class ASTTimerNode extends ASTNode {
    ASTNode interpret(PTInterpreter interpreter) { return this; }
}

class ASTLoopNode extends ASTNode {
    ASTNode interpret(PTInterpreter interpreter) { return this; }
}

class ASTStopwatchNode extends ASTNode {
    ASTNode interpret(PTInterpreter interpreter) { return this; }
}

public class Program {
    String name;
    ASTNode root;
    public Program (String filename) {
        // Parse xml-file with DOM
        // ...
    }
}