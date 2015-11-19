package com.elovirta.dita;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * DITA-OT processer factory. Not thread-safe, but can be reused.
 *
 * @since 1.0
 */
public class ProcessorFactory {

    private final File ditaDir;
    private final Map<String, String> args = new HashMap<>();

    private ProcessorFactory(final File ditaDir) {
        this.ditaDir = ditaDir;
    }

    /**
     * Get factory instance.
     *
     * @param ditaDir absolute DITA-OT directory
     * @since 1.0
     */
    public static ProcessorFactory newInstance(final File ditaDir) {
        if (!ditaDir.isAbsolute()) {
            throw new IllegalArgumentException();
        }
        return new ProcessorFactory(ditaDir);
    }

    /**
     * Set temporary directory.
     *
     * @param tmp absolute temporary directory
     * @since 1.0
     */
    public void setTempDir(final File tmp) {
        if (!tmp.isAbsolute()) {
            throw new IllegalArgumentException();
        }
        args.put("base.temp.dir", tmp.getAbsolutePath());
    }

    /**
     * Create new processor.
     *
     * @param transtype transformation type
     * @since 1.0
     */
    public Processor newProcessor(final String transtype) {
        if (ditaDir == null) {
            throw new IllegalStateException();
        }
        return new Processor(ditaDir, transtype, Collections.unmodifiableMap(args));
    }

}
