package ru.spbstu.ptime.interpreter;

import java.io.PrintStream;
import java.util.Date;

public class ASTInterpreterXML implements ASTInterpreter {
    private String indent;
    private PrintStream stream;
    public ASTInterpreterXML() {
        this(System.out);
    }
    public ASTInterpreterXML(PrintStream stream) {
        indent = "";
        this.stream = stream;
    }
    public void setPrintStream(PrintStream stream) {
        this.stream = stream;
    }
    public void runTimer(Date date) {
        System.out.format("%s<timer time=\"%s\"/>%n", indent, ASTBuilder.DATE_FORMAT.format(date));
    }
    public void runTimer(long seconds) {
        stream.format("%s<timer interval=\"%d\"/>%n", indent, seconds);
    }
    public void runStopwatch() {
        stream.format("%s<stopwatch/>%n", indent);
    }
    public void runLoop(ASTNode body, int iterations) {
        stream.format("%s<loop iterations=\"%d\"/>%n", indent, iterations);
        run(body);
        stream.format("%s</loop>%n", indent);
    }
    public void run(ASTNode body) {
        String oldIndent = indent;
        indent = "    " + indent;
        ASTNode cur = body;
        while (null != cur)
            cur = cur.interpret(this);
        indent = oldIndent;
    }
    public void run(Program program) {
        stream.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        stream.format("<program name=\"%s\">%n", program.getName());
        run(program.getBody());
        stream.println("</program>");
    }
}