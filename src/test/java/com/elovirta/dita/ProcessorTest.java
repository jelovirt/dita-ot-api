package com.elovirta.dita;

import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.helpers.NOPLogger;

import java.io.File;

public class ProcessorTest {

    @Rule
    public final TemporaryFolder tmpDir = new TemporaryFolder();

    private Processor p;

    @Before
    public void setUp() throws Exception {
        final String ditaDir = System.getProperty("dita.dir");
        if (ditaDir == null) {
            throw new IllegalArgumentException("dita.dir system property not set");
        }
        final ProcessorFactory pf = ProcessorFactory.newInstance(new File(ditaDir));
        pf.setTempDir(tmpDir.newFolder("tmp"));
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
        final File mapFile = new File(getClass().getClassLoader().getResource("test.ditamap").toURI());
        p.setInput(mapFile);
        p.setOutput(tmpDir.newFolder("out"));
        p.setLogger(NOPLogger.NOP_LOGGER);
        //p.setLogger(LoggerFactory.getLogger(this.getClass()));
        p.run();
    }

}
