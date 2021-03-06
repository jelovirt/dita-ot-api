package com.elovirta.dita;

import org.apache.tools.ant.*;
import org.slf4j.Logger;

import java.io.File;
import java.io.PrintStream;
import java.net.URI;
import java.util.*;
import java.util.regex.Pattern;

/**
 * DITA-OT processer. Not thread-safe, but can be reused.
 *
 * @since 1.0
 */
public class Processor {

    private File ditaDir;
    private Map<String, String> args;
    private Logger logger;

    Processor(final File ditaDir, final String transtype, final Map<String, String> args) {
        this.ditaDir = ditaDir;
        this.args = new HashMap<>(args);
        this.args.put("dita.dir", ditaDir.getAbsolutePath());
        this.args.put("transtype", transtype);
    }

    /**
     * Set input document.
     *
     * @param input absolute input document
     * @since 1.0
     */
    @Deprecated
    public void setInput(final File input) {
        if (!input.isAbsolute()) {
            throw new IllegalArgumentException();
        }
        setInput(input.toURI());
    }

    /**
     * Set input document.
     *
     * @param input absolute input document
     * @since 1.0
     */
    public void setInput(final URI input) {
        if (!input.isAbsolute()) {
            throw new IllegalArgumentException();
        }
        args.put("args.input", input.toString());
    }

    /**
     * Set output document.
     *
     * @param output absolute output document
     * @since 1.0
     */
    @Deprecated
    public void setOutput(final File output) {
        if (!output.isAbsolute()) {
            throw new IllegalArgumentException();
        }
        args.put("output.dir", output.getAbsolutePath());
    }

    /**
     * Set output document.
     *
     * @param output absolute output document
     * @since 1.0
     */
    public void setOutput(final URI output) {
        if (!output.isAbsolute()) {
            throw new IllegalArgumentException();
        }
        if (!output.getScheme().equals("file")) {
            throw new IllegalArgumentException("Only file scheme allowed as output directory URI");
        }
        args.put("output.dir", output.toString());
    }

    /**
     * Set property.
     *
     * @param name property name
     * @param value property value
     * @since 1.0
     */
    public void setProperty(final String name, final String value) {
        args.put(name, value);
    }

    /**
     * Set logger.
     *
     * @param logger message logger
     * @since 1.0
     */
    public void setLogger(final Logger logger) {
        this.logger = logger;
    }

    /**
     * Execute conversion.
     *
     * @since 1.0
     */
    public void run() {
        if (!args.containsKey("args.input")) {
            throw new IllegalStateException();
        }
//        final PrintStream savedErr = System.err;
//        final PrintStream savedOut = System.out;
        try {
            final File buildFile = new File(ditaDir, "build.xml");
            final Project project = new Project();
            project.setCoreLoader(this.getClass().getClassLoader());
            if (logger != null) {
                project.addBuildListener(new LoggerListener(logger));
            }
//            System.setOut(new PrintStream(new DemuxOutputStream(project, false)));
//            System.setErr(new PrintStream(new DemuxOutputStream(project, true)));
            project.fireBuildStarted();
            project.init();
            project.setBaseDir(ditaDir);
            project.setKeepGoingMode(false);
            for (final Map.Entry<String, String> arg : args.entrySet()) {
                project.setUserProperty(arg.getKey(), arg.getValue());
            }
            ProjectHelper.configureProject(project, buildFile);
            final Vector<String> targets = new Vector<>();
            targets.addElement(project.getDefaultTarget());
            project.executeTargets(targets);
        } finally {
//            System.setOut(savedOut);
//            System.setErr(savedErr);
        }
    }

    static class LoggerListener implements BuildListener {

        private final Pattern fatalPattern = Pattern.compile("\\[\\w+F\\]\\[FATAL\\]");
        private final Pattern errorPattern = Pattern.compile("\\[\\w+E\\]\\[ERROR\\]");
        private final Pattern warnPattern = Pattern.compile("\\[\\w+W\\]\\[WARN\\]");
        private final Pattern infoPattern = Pattern.compile("\\[\\w+I\\]\\[INFO\\]");
        private final Pattern debugPattern = Pattern.compile("\\[\\w+D\\]\\[DEBUG\\]");

        final Logger logger;

        public LoggerListener(final Logger logger) {
            this.logger = logger;
        }

        @Override
        public void buildStarted(BuildEvent event) {
            //System.out.println("build started: " + event.getMessage());
        }

        @Override
        public void buildFinished(BuildEvent event) {
            //System.out.println("build finished: " + event.getMessage());
        }

        @Override
        public void targetStarted(BuildEvent event) {
            //System.out.println(event.getTarget().getName() + ":");
        }

        @Override
        public void targetFinished(BuildEvent event) {
            //System.out.println("target finished: " + event.getTarget().getName());
        }

        @Override
        public void taskStarted(BuildEvent event) {
            //System.out.println("task started: " + event.getTask().getTaskName());
        }

        @Override
        public void taskFinished(BuildEvent event) {
            //System.out.println("task finished: " + event.getTask().getTaskName());
        }

        @Override
        public void messageLogged(BuildEvent event) {
            final String message = event.getMessage();
            int level;
            if (fatalPattern.matcher(message).find()) {
                level = Project.MSG_ERR;
            } else if (errorPattern.matcher(message).find()) {
                level = Project.MSG_ERR;
            } else if (warnPattern.matcher(message).find()) {
                level = Project.MSG_WARN;
            } else if (infoPattern.matcher(message).find()) {
                level = Project.MSG_INFO;
            } else if (debugPattern.matcher(message).find()) {
                level = Project.MSG_DEBUG;
            } else {
                level = event.getPriority();
            }
            switch (level) {
                case Project.MSG_DEBUG:
                    logger.trace(message);
                    break;
                case Project.MSG_VERBOSE:
                    logger.debug(message);
                    break;
                case Project.MSG_INFO:
                    logger.info(message);
                    break;
                case Project.MSG_WARN:
                    logger.warn(message);
                    break;
                default:
                    logger.error(message);
            }
        }
    }
}
