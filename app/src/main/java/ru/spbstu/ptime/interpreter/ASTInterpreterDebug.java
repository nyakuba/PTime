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
    public void runTimer(ASTTimerByTimeNode timerNode) {
        System.out.printf("Timer [Date = %s]\n", timerNode.getDate());
    }
    public void runTimer(ASTTimerByIntervalNode timerNode) {
        System.out.printf("Time [Interval = %d seconds]\n", timerNode.getSeconds());
    }
    public void runStopwatch(ASTStopwatchNode stopwatchNode) {
        System.out.print("Stopwatch\n");
    }
//    public void stopTimeProcess() {}
    public void runLoop(ASTLoopNode loopNode) {
        int iterations = loopNode.getIterations();
        if (iterations == -1)
            while (true)
                run(loopNode.getBody());
        else
            while (-1 != --iterations)
                run(loopNode.getBody());
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
