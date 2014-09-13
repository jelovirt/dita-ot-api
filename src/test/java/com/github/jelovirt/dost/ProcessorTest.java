package com.github.jelovirt.dost;

import static org.junit.Assert.fail;

import org.slf4j.LoggerFactory;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.helpers.NOPLogger;
import org.slf4j.impl.SimpleLogger;

import java.io.File;

/**
 * Created by jelovirt on 20.8.2014.
 */
public class ProcessorTest {

    private final File ditaDir = new File("/Users/jelovirt/Work/github/dita-ot/src/main");
    private final File srcDir = new File("/Users/jelovirt/Work/github/dita-ot-api/src/test/resources");
    private final File tmpDir = new File("/Users/jelovirt/Temp");

    private Processor p;

    @Before
    public void setUp() throws Exception {
        final ProcessorFactory pf = ProcessorFactory.newInstance();
        pf.setDitaDir(ditaDir);
        p = pf.newProcessor("html5");
    }

    @Test
    public void testRunWithoutArgs() throws Exception {
        try {
            p.run();
            fail();
        } catch (final IllegalStateException e) {}
    }

    @Test
    public void testRun() throws Exception {
        p.setInput(new File(srcDir, "test.ditamap"));
        p.setOutput(tmpDir);
        //p.setLogger( NOPLogger.NOP_LOGGER);
        p.setLogger(LoggerFactory.getLogger(this.getClass()));
        p.run();
    }

    public static void main(final String[] args) {
        final ProcessorTest pt = new ProcessorTest();
        try {
            pt.setUp();
            pt.testRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
