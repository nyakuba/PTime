package ru.spbstu.ptime;

import org.junit.Test;

import java.util.Date;

import ru.spbstu.ptime.interpreter.ASTLoopNode;
import ru.spbstu.ptime.interpreter.ASTNode;
import ru.spbstu.ptime.interpreter.ASTStopwatchNode;
import ru.spbstu.ptime.interpreter.ASTTimerByIntervalNode;
import ru.spbstu.ptime.interpreter.ASTTimerByTimeNode;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ASTNodeTest {
    @Test
    public void testASTStopwatchNodeEquals() {
        ASTNode stopwatchNode0 = new ASTStopwatchNode();
        assertEquals(stopwatchNode0, stopwatchNode0);
        assertEquals(stopwatchNode0.hashCode(), stopwatchNode0.hashCode());

        ASTNode stopwatchNode1 = new ASTStopwatchNode();
        assertEquals(stopwatchNode0, stopwatchNode1);
        assertEquals(stopwatchNode0.hashCode(), stopwatchNode1.hashCode());
    }

    @Test
    public void testASTTimerByIntervalNodeEquals() {
        long value = randomInt();
        ASTNode timer0 = new ASTTimerByIntervalNode(value);
        assertEquals(timer0, timer0);
        assertEquals(timer0.hashCode(), timer0.hashCode());

        ASTNode timer1 = new ASTTimerByIntervalNode(value);
        ASTNode timer2 = new ASTTimerByIntervalNode(value + randomInt() + 1);

        assertEquals(timer0, timer1);
        assertEquals(timer0.hashCode(), timer1.hashCode());

        assertNotEquals(timer0, timer2);
        assertNotEquals(timer0.hashCode(), timer2.hashCode());
    }

    @Test
    public void testASTTimerByTimeNode() {
        long ms = randomInt();
        ASTNode timer0 = new ASTTimerByTimeNode(new Date(ms));
        assertEquals(timer0, timer0);
        assertEquals(timer0.hashCode(), timer0.hashCode());

        ASTNode timer1 = new ASTTimerByTimeNode(new Date(ms));
        ASTNode timer2 = new ASTTimerByTimeNode(new Date(ms + randomInt() + 1));

        assertEquals(timer0, timer1);
        assertEquals(timer0.hashCode(), timer1.hashCode());

        assertNotEquals(timer0, timer2);
        assertNotEquals(timer0.hashCode(), timer2.hashCode());
    }

    @Test
    public void testASTLoopNode() {
        long value = randomInt();
        int iterations = (int) randomInt();
        ASTNode timer0 = new ASTTimerByIntervalNode(value);
        ASTNode loop0 = new ASTLoopNode(timer0, iterations);
        assertEquals(loop0, loop0);
        assertEquals(loop0.hashCode(), loop0.hashCode());

        ASTNode timer1 = new ASTTimerByIntervalNode(value);
        ASTNode loop1 = new ASTLoopNode(timer1, iterations);
        assertEquals(loop0, loop1);
        assertEquals(loop0.hashCode(), loop1.hashCode());

        ASTNode timer2 = new ASTTimerByIntervalNode(value + randomInt() + 1);
        ASTNode loop2 = new ASTLoopNode(timer2, iterations);
        assertNotEquals(loop0, loop2);
        assertNotEquals(loop0.hashCode(), loop2.hashCode());

        ASTNode loop3 = new ASTLoopNode(timer0, iterations + (int) randomInt() + 1);
        assertNotEquals(loop0, loop3);
        assertNotEquals(loop0.hashCode(), loop3.hashCode());

        ASTNode stopwatch0 = new ASTStopwatchNode();
        ASTNode loop4 = new ASTLoopNode(stopwatch0, iterations);
        assertNotEquals(loop0, loop4);
        assertNotEquals(loop0.hashCode(), loop4.hashCode());
    }

    public void testASTNodeEquals() {
        ASTNode node0 = new ASTStopwatchNode();
        ASTNode node1 = new ASTStopwatchNode();
        long value = randomInt();
        ASTNode node2 = new ASTTimerByIntervalNode(value);
        ASTNode node3 = new ASTTimerByIntervalNode(value);

        node0.setNext(node2);
        node1.setNext(node3);
        assertEquals(node0, node1);
        assertEquals(node0.hashCode(), node1.hashCode());

        ASTNode node4 = new ASTStopwatchNode();
        ASTNode node5 = new ASTStopwatchNode();
        node4.setNext(node5);
        assertNotEquals(node0, node5);
        assertNotEquals(node0.hashCode(), node5.hashCode());

        node2.setNext(node5);
        assertNotEquals(node0, node1);
        assertNotEquals(node0.hashCode(), node1.hashCode());
    }

    private long randomInt() {
       return (long) Math.random() * Long.MAX_VALUE;
    }

    private long randomInt(long lo, long hi) {
        return lo + randomInt()*(hi - lo + 1);
    }
}