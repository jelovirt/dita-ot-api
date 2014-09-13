package com.github.jelovirt.dost;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.io.File;

/**
 * Created by jelovirt on 20.8.2014.
 */
public class ProcessorFactoryTest {

    private final File ditaDir = new File("/Users/jelovirt/Work/github/dita-ot/src/main");

    @Test
    public void testNewInstance() {
        assertNotNull(ProcessorFactory.newInstance());
    }

    @Test
    public void testNewProcessor() {
        final ProcessorFactory pf = ProcessorFactory.newInstance();
        try {
            pf.newProcessor("html5");
            fail();
        } catch (final IllegalStateException e) {}
        pf.setDitaDir(ditaDir);
        assertNotNull(pf.newProcessor("html5"));

    }

}
