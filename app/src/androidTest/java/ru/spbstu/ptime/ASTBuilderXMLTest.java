package ru.spbstu.ptime;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;

import ru.spbstu.ptime.interpreter.ASTBuilderXML;
import ru.spbstu.ptime.interpreter.ASTNode;
import ru.spbstu.ptime.interpreter.ASTStopwatchNode;
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
    public void testExampleProgramStructure() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        // loading program from test file
        InputStream testFile = appContext.getResources().getAssets().open("test/program1.xml");
        ASTBuilderXML builder = new ASTBuilderXML();
        builder.build(testFile);
        Program program = builder.getProgram();

        ASTNode root = new ASTStopwatchNode();
        Program expectedProgram = new Program("prog", root);

        assertEquals(program, expectedProgram);
    }
}
