package ru.spbstu.ptime.interpreter;

import java.util.Date;

import ru.spbstu.ptime.constructor.ItemAdapter;
import ru.spbstu.ptime.constructor.items.LoopEndItem;
import ru.spbstu.ptime.constructor.items.LoopStartItem;
import ru.spbstu.ptime.constructor.items.StopwatchItem;
import ru.spbstu.ptime.constructor.items.TimerByIntervalItem;
import ru.spbstu.ptime.interpreter.ASTInterpreter;

/**
 * Created by venedikttsulenev on 07/12/16.
 */

public class ASTInterpreterUIEdit implements ASTInterpreter {
    private ItemAdapter itemAdapter;
    public ASTInterpreterUIEdit(ItemAdapter itemAdapter) {
        this.itemAdapter = itemAdapter;
    }
    public void runTimer(ASTTimerByTimeNode timerNode) {
        // ...
    }
    public void runTimer(ASTTimerByIntervalNode timerNode) {
        itemAdapter.addItem(new TimerByIntervalItem(timerNode.getSeconds()));
    }
    public void runStopwatch(ASTStopwatchNode stopwatchNode) {
        itemAdapter.addItem(new StopwatchItem());
    }
    public void runLoop(ASTLoopNode loopNode) {
        itemAdapter.addItem(new LoopStartItem(loopNode.getIterations()));
        run(loopNode.getBody());
        itemAdapter.addItem(new LoopEndItem());
    }
    public void run(ASTNode body) {
        while (null != body)
            body = body.interpret(this);
    }
    public void run(Program program) {
        run(program.getBody());
        itemAdapter.notifyDataSetChanged();
    }
}
