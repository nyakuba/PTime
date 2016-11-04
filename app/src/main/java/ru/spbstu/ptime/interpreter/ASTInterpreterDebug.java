package ru.spbstu.ptime.interpreter;

import java.io.File;
import java.util.Date;

public class ASTInterpreterDebug implements ASTInterpreter {
    public static void main(String args[]) {
        ASTBuilderXML builder = new ASTBuilderXML();
        ASTInterpreterDebug interpreter = new ASTInterpreterDebug();
        File xmlFile = new File(args[0]);
        builder.build(xmlFile);
        Program program = builder.getProgram();
        if (program != null) {
            System.out.println("Program \"" + program.getName() + "\":");
            interpreter.run(program.getBody());
        }
        else
            System.out.println("Failed to parse \"" + args[0] + "\" :CCCC");
    }
    public void runTimer(Date date) {
        System.out.printf("Timer [Date = %s]\n", date);
    }
    public void runTimer(long seconds) {
        System.out.printf("Time [Interval = %d seconds]\n", seconds);
    }
    public void runStopwatch() {
        System.out.print("Stopwatch\n");
    }
    public void runLoop(ASTNode body, int iterations) {
        if (iterations == -1)
            while (true)
                run(body);
        else
            while (-1 != --iterations)
                run(body);
    }
    public void run(ASTNode body) {
        ASTNode cur = body;
        while (cur != null)
            cur = cur.interpret(this);
    }
}
