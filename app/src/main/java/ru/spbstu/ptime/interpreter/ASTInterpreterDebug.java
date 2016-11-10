package ru.spbstu.ptime.interpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

public class ASTInterpreterDebug implements ASTInterpreter {
    public static void main(String args[]) {
        ASTBuilderXML builder = new ASTBuilderXML();
        ASTInterpreter interpreter = new ASTInterpreterXML();
        File xmlFile = new File(args[0]);
        try {
            InputStream stream = new FileInputStream(xmlFile);
            builder.build(stream);
            Program program = builder.getProgram();
            if (program != null)
                interpreter.run(program);
            else
                System.out.println("Failed to parse \"" + args[0] + "\" :CCCC");
        } catch (FileNotFoundException e) {
            System.out.println("Cannot open file " + args[0]);
        }
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
    public void run(Program program) {
        System.out.format("Program \"%s\":\n", program.getName());
        run(program.getBody());
    }
}
