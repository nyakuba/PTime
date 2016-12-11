package ru.spbstu.ptime.interpreter;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ASTInterpreterXML implements ASTInterpreter {
    private static DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
    private String indent;
    private PrintStream stream;
    public ASTInterpreterXML() {
        this(System.out);
    }
    public ASTInterpreterXML(PrintStream stream) {
        indent = "";
        this.stream = stream;
    }
    public void runTimer(ASTTimerByTimeNode timerNode) {
        stream.format("%s<timer time=\"%s\"/>%n", indent, DATE_FORMAT.format(timerNode.getDate()));
    }
    public void runTimer(ASTTimerByIntervalNode timerNode) {
        stream.format("%s<timer interval=\"%d\"/>%n", indent, timerNode.getSeconds());
    }
    public void runStopwatch(ASTStopwatchNode stopwatchNode) {
        stream.format("%s<stopwatch/>%n", indent);
    }
    public void runLoop(ASTLoopNode loopNode) {
        stream.format("%s<loop iterations=\"%d\">%n", indent, loopNode.getIterations());
        run(loopNode.getBody());
        stream.format("%s</loop>%n", indent);
    }
    public void run(ASTNode body) {
        String oldIndent = indent;
        indent = "    " + indent;
        while (null != body)
            body = body.interpret(this);
        indent = oldIndent;
    }
    public void run(Program program) {
        stream.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        stream.format("<program name=\"%s\">%n", program.getName());
        run(program.getBody());
        stream.println("</program>");
        stream.flush();
        stream.close();
    }
}