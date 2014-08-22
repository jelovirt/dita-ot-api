package com.github.jelovirt.dost;

import java.io.File;

/**
 * Created by jelovirt on 20.8.2014.
 */
public class ProcessorFactory {

    private File ditaDir;

    private ProcessorFactory() {
    }

    public static ProcessorFactory newInstance() {
        return new ProcessorFactory();
    }

    public void setDitaDir(final File ditaDir) {
        this.ditaDir = ditaDir;
    }

    public Processor newProcessor(final String transtype) {
        if (ditaDir == null) {
            throw new IllegalStateException();
        }
        return new Processor(ditaDir, transtype);
    }

}
