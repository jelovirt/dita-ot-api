package com.elovirta.dita;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.io.File;

public class ProcessorFactoryTest {

    @Test
    public void testNewInstance() {
        final String ditaDir = System.getProperty("dita.dir");
        if (ditaDir == null) {
            throw new IllegalArgumentException("dita.dir system property not set");
        }
        assertNotNull(ProcessorFactory.newInstance(new File(ditaDir)));
    }

    @Test
    public void testNewProcessor() {
        final String ditaDir = System.getProperty("dita.dir");
        if (ditaDir == null) {
            throw new IllegalArgumentException("dita.dir system property not set");
        }
        final ProcessorFactory pf = ProcessorFactory.newInstance(new File(ditaDir));
        assertNotNull(pf.newProcessor("html5"));
    }

}
