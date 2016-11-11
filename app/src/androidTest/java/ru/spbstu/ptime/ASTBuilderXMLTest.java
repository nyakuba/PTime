package ru.spbstu.ptime;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ru.spbstu.ptime.interpreter.ASTBuilderXML;
import ru.spbstu.ptime.interpreter.ASTLoopNode;
import ru.spbstu.ptime.interpreter.ASTNode;
import ru.spbstu.ptime.interpreter.ASTStopwatchNode;
import ru.spbstu.ptime.interpreter.ASTTimerByIntervalNode;
import ru.spbstu.ptime.interpreter.ASTTimerByTimeNode;
import ru.spbstu.ptime.interpreter.Program;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ASTBuilderXMLTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("ru.spbstu.ptime", appContext.getPackageName());
    }

    @Test
    public void testExampleProgram() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        // loading program from test file
        InputStream testFile = appContext.getResources().getAssets().open("test/program1.xml");
        ASTBuilderXML builder = new ASTBuilderXML();
        builder.build(testFile);
        Program program = builder.getProgram();

        // check if program name is parsed correctly
        assertNotNull(program);
        assertEquals(program.getName(), "prog");
    }

    @Test
    public void testBuildXMLStopwatch() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        // loading program from test file
        InputStream testFile = appContext.getResources().getAssets().open("test/stopwatch.xml");
        ASTBuilderXML builder = new ASTBuilderXML();
        builder.build(testFile);
        Program program = builder.getProgram();

        ASTNode stopwatch = new ASTStopwatchNode();
        Program expectedProgram = new Program("stopwatch", stopwatch);

        assertEquals(program, expectedProgram);
    }

    @Test
    public void testBuildXMLTimer0() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        // loading program from test file
        InputStream testFile = appContext.getResources().getAssets().open("test/timer0.xml");
        ASTBuilderXML builder = new ASTBuilderXML();
        builder.build(testFile);
        Program program = builder.getProgram();

        ASTNode timer = new ASTTimerByTimeNode(getDate(10, 59, 59));
        Program expectedProgram = new Program("timer0", timer);

        assertEquals(program, expectedProgram);
    }

    @Test
    public void testBuildXMLTimer1() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        // loading program from test file
        InputStream testFile = appContext.getResources().getAssets().open("test/timer1.xml");
        ASTBuilderXML builder = new ASTBuilderXML();
        builder.build(testFile);
        Program program = builder.getProgram();

        ASTNode timer = new ASTTimerByIntervalNode(1024);
        Program expectedProgram = new Program("timer1", timer);

        assertEquals(program, expectedProgram);
    }

    @Test
    public void testExampleProgramStructure() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        // loading program from test file
        InputStream testFile = appContext.getResources().getAssets().open("test/program1.xml");
        ASTBuilderXML builder = new ASTBuilderXML();
        builder.build(testFile);
        Program program = builder.getProgram();

        // form expected program
        ASTNode timer0 = new ASTTimerByTimeNode(getDate(23, 43, 15));
        ASTNode timer1 = new ASTTimerByIntervalNode(5);
        ASTNode inner_loop = new ASTLoopNode(timer1, 3);
        ASTNode stopwatch0 = new ASTStopwatchNode();
        timer0.setNext(inner_loop);
        inner_loop.setNext(stopwatch0);
        ASTNode outer_loop = new ASTLoopNode(timer0, 25);

        Program expectedProgram = new Program("prog", outer_loop);

        assertEquals(program, expectedProgram);
    }

    private Date getDate(int hh, int mm, int ss) {
        Date current = new Date();                   /* Текущее время */
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, hh);      /* Заданные часы */
        calendar.set(Calendar.MINUTE, mm);           /* минуты */
        calendar.set(Calendar.SECOND, ss);           /* секунды */
        if (current.after(calendar.getTime()))       /* Если сегодня заданное время уже не наступит, */
            calendar.roll(Calendar.DATE, true);      /* переносим на завтра. */
        return calendar.getTime();
    }
}
