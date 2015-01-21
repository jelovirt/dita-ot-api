package com.jelovirt.dita;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * DITA-OT processer factory. Not thread-safe, but can be reused.
 */
public class ProcessorFactory {

    private final File ditaDir;
    private final Map<String, String> args = new HashMap<String, String>();

    private ProcessorFactory(final File ditaDir) {
        this.ditaDir = ditaDir;
    }

    public static ProcessorFactory newInstance(final File ditaDir) {
        return new ProcessorFactory(ditaDir);
    }

    public void setTempDir(final File tmp) {
        args.put("base.temp.dir", tmp.getAbsolutePath());
    }

    public Processor newProcessor(final String transtype) {
        if (ditaDir == null) {
            throw new IllegalStateException();
        }
        return new Processor(ditaDir, transtype, Collections.unmodifiableMap(args));
    }

}
