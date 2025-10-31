package com.marcoscherzer.msimpleserver.util.logging;

import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.MLogHeaderFieldType.TIMEFIELD;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
Author Marco Scherzer (Description,Definition) with Microsoft Copilot,
PostAuthor, Ideas & Architectures Marco Scherzer
Copyright Marco Scherzer, All rights reserved
*/
public final class MThreadLocalPrintStream {
    // ThreadLocal to store a MStringBuilder for each thread
    //private static final ThreadLocal<MStringBuilder> threadBuffer = ThreadLocal.withInitial(MStringBuilder::new);

    // Scheduled executor service for cleanup tasks
    private static final ScheduledExecutorService cleanupExecutor;
    // Map to store registered threads and their corresponding MStringBuilders
    private static final java.util.concurrent.ConcurrentHashMap<Thread, MDualStringBuffer> registeredBuffers = new ConcurrentHashMap();
    private static final ExecutorService outputPool;
    public static MThreadLocalPrintStream_ mout = new MThreadLocalPrintStream_();
    public static MThreadLocalPrintStream_ mfull = new MThreadLocalPrintStream_();//dbg incl err
    private static PrintStream out = System.out;
    private static PrintStream full = System.out;
    private static MGlobalLogMode activeGlobalLogMode;

    static {
        cleanupExecutor = Executors.newScheduledThreadPool(1);
        //startCleanupTask();
        //registerShutdownHook();
        outputPool = Executors.newFixedThreadPool(1);
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private MThreadLocalPrintStream() {
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void setOutAndErr(PrintStream outstream, PrintStream errstream) {
        out = outstream;
        full = errstream;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Bei Full werden out und full auf gesetzten Stream für err geloggt
     */
    public static void setLogMode(MGlobalLogMode logMode) {
        activeGlobalLogMode = logMode;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void setLogHeader(MLogHeader logHeader) {

        getBufferForCurrentThread().outBuffer.setLogHeader(logHeader);
        getBufferForCurrentThread().fullBuffer.setLogHeader(logHeader);
                        /*
                        registerThreadIfNeeded();
                        return registeredBuffers.get(Thread.currentThread());
                        */
    }

    /**
     * Retrieves the output for a specific thread.
     * Copyright Marco Scherzer, All rights reserved
     * Author Marco Scherzer
     *
     * @param thread The thread for which the output is retrieved
     * @return The output for the specified thread as a string
     */
    public static String getOutput(Thread thread) {
        synchronized (registeredBuffers) {
            MDualStringBuffer buffer = registeredBuffers.get(thread);
            return buffer != null ? buffer.toString() : "";
        }
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Automatically register a new thread with its own MStringBuilder
     */

    private static void registerThreadIfNeeded() {
        Thread currentThread = Thread.currentThread();
        synchronized (registeredBuffers) {
            if (!registeredBuffers.containsKey(currentThread)) {
                MDualStringBuffer sb = new MDualStringBuffer();
                registeredBuffers.put(currentThread, sb);
            }
        }
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Get the MStringBuilder for the current thread
     */

    private static synchronized MDualStringBuffer getBufferForCurrentThread() {
        registerThreadIfNeeded();
        return registeredBuffers.get(Thread.currentThread());
    }

    /**
     * Starts the cleanup task using ScheduledExecutorService.
     * Copyright Marco Scherzer, All rights reserved
     * Author Marco Scherzer
     */
    private static void startCleanupTask() {
        cleanupExecutor.scheduleAtFixedRate(() -> {
            synchronized (registeredBuffers) {
                Iterator<Map.Entry<Thread, MDualStringBuffer>> iterator = registeredBuffers.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Thread, MDualStringBuffer> entry = iterator.next();
                    if (!entry.getKey().isAlive()) {
                        iterator.remove();
                    }
                }
            }
        }, 10, 10, TimeUnit.SECONDS); // Initial delay of 10 seconds, then every 10 seconds
    }

    /**
     * Registers a shutdown hook to clean up resources.
     * Copyright Marco Scherzer, All rights reserved
     * Author Marco Scherzer
     */
    private static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdown();
        }));
    }

    /**
     * Shuts down the executor service.
     * Copyright Marco Scherzer, All rights reserved
     * Author Marco Scherzer
     */
    public static void shutdown() {
        cleanupExecutor.shutdown();
        outputPool.shutdown();
        try {
            if (!cleanupExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                cleanupExecutor.shutdownNow();
            }
            if (!outputPool.awaitTermination(5, TimeUnit.SECONDS)) {
                outputPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            cleanupExecutor.shutdownNow();
            outputPool.shutdownNow();
            //Thread.currentThread().interrupt();
        }
    }

    /**
     * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
     */
    public static void main(String[] args) {
        System.out.println("main");
        //MThreadLocalPrintStream.setLogHeader(new MLogHeader().addField("@", OUTPUTTIME, ": "));//current Thread; später get/setLogHeader in MStringBuilder
        // Create a thread pool with 10 threads
        MThreadLocalPrintStream.setOutAndErr(System.out, System.err);
        MThreadLocalPrintStream.setLogMode(MGlobalLogMode.logOutAndFullToSetupedErr);
        //MThreadLocalPrintStream.activateFullLoggingToSetupedErrStream(true);
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // Submit 10 tasks to the executor service
        for (int i = 1; i <= 10; i++) {
            int threadNumber = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("run1");
                    MThreadLocalPrintStream.setLogHeader(new MLogHeader().addField("@", TIMEFIELD, ": "));
                    System.out.println("run2");
                    for (int j = 0; j < 100000; j++) {
                        mout.println(j + ". Hello from Thread " + threadNumber);
                        mfull.println(j + ". Additional Full Hello from Thread " + threadNumber);
                        //try {Thread.sleep((long)Math.random()*1000);} catch (InterruptedException exc) {exc.printStackTrace();}
                    }
                    mout.flushBufferToTargetStream();
                }
            });
        }

        // Shutdown the executor service after all tasks are completed
        executorService.shutdown();
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public enum MGlobalLogMode {logOutToSetupedOut, logFullToSetupedErr, logOutAndFullToSetupedErr}

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public enum MLogHeaderFieldType {THREADNAME, TIMEFIELD}

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static class MDualStringBuffer {
        MStringBuilder outBuffer = new MStringBuilder();
        MStringBuilder fullBuffer = new MStringBuilder();
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final class MLogHeaderField {
        private final String preFix;
        private final String postFix;
        private final MLogHeaderFieldType type;

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public MLogHeaderField(String preFix, MLogHeaderFieldType type, String postFix) {
            this.preFix = preFix;
            this.postFix = postFix;
            this.type = type;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        private String getPrefFix() {
            return preFix;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        private String getPostFix() {
            return postFix;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        private MLogHeaderFieldType getType() {
            return type;
        }
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final static class MLogHeader {
        private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        private final ArrayList<Object> headers = new ArrayList();

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public MLogHeader addStringConstant(String logHeaderStringConstant) {
            headers.add(logHeaderStringConstant);
            return this;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public MLogHeader addField(String preFix, MLogHeaderFieldType logHeaderInfo, String postFix) {
            headers.add(new MLogHeaderField(preFix, logHeaderInfo, postFix));
            return this;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public MLogHeader addThreadField(String preFix, String postFix) {
            addField(preFix, MLogHeaderFieldType.THREADNAME, postFix);
            return this;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public MLogHeader addTimeField(String preFix, String postFix) {
            addField(preFix, MLogHeaderFieldType.TIMEFIELD, postFix);
            return this;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public String create() {
            StringBuffer sb = new StringBuffer();
            for (Object o : headers) {
                if (o instanceof MLogHeaderField) {
                    MLogHeaderField o_ = (MLogHeaderField) o;
                    switch (o_.getType()) {
                        case TIMEFIELD:
                            sb.append(o_.getPrefFix()).append(sdf.format(new Date(System.currentTimeMillis()))).append(o_.getPostFix());
                            break;
                        case THREADNAME:
                            sb.append(o_.getPrefFix()).append(Thread.currentThread()).append(o_.getPostFix());
                            break;
                    }
                } else sb.append(o);
            }
            return sb.toString();
        }
    }

    /**
     * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
     */
    public static final class MThreadLocalPrintStream_ extends PrintStream {


        /**
         * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
         */
        private MThreadLocalPrintStream_() {
            super(new MNoOutputStream());
        }

        /**
         * Flushes buffer to the target output stream and clears the buffer.
         * Copyright Marco Scherzer, All rights reserved Author Marco Scherzer
         * aufruf auf stream intelisense unterstützung, flushstream über mode/ eifnaches int wählbar statt durch stream driektübergabe.
         *
         * @param targetOutputStream The output stream where the buffer is
         *                           flushed
         */
        public void flushBufferToTargetStream() {
            System.out.println("flushBufferToTargetStream fullLoggingActive " + activeGlobalLogMode);
            outputPool.submit(new Runnable() {
                final MDualStringBuffer buffer = getBufferForCurrentThread();

                public void run() {
                    System.out.println("logJob running");
                    try {
                        if (activeGlobalLogMode == MGlobalLogMode.logOutToSetupedOut) {
                            MThreadLocalPrintStream.out.write(buffer.outBuffer.toString().getBytes(StandardCharsets.UTF_8));
                            MThreadLocalPrintStream.out.flush();
                        } else if (activeGlobalLogMode == MGlobalLogMode.logFullToSetupedErr) { //falls fullLoggingActive für out oder full zusätzlich nach full loggen
                            MThreadLocalPrintStream.full.write(buffer.fullBuffer.toString().getBytes(StandardCharsets.UTF_8));
                            MThreadLocalPrintStream.full.flush();
                        } else if (activeGlobalLogMode == MGlobalLogMode.logOutAndFullToSetupedErr) {
                            MThreadLocalPrintStream.full.write(buffer.outBuffer.toString().getBytes(StandardCharsets.UTF_8));
                            MThreadLocalPrintStream.full.flush();
                            MThreadLocalPrintStream.full.write(buffer.fullBuffer.toString().getBytes(StandardCharsets.UTF_8));
                            MThreadLocalPrintStream.full.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace(MThreadLocalPrintStream.full);
                    } finally {
                        // Clear the buffer for the current thread
                        if (activeGlobalLogMode == MGlobalLogMode.logOutToSetupedOut) {
                            buffer.outBuffer.setLength(0);
                        } else if (activeGlobalLogMode == MGlobalLogMode.logFullToSetupedErr) {
                            buffer.fullBuffer.setLength(0);
                        } else if (activeGlobalLogMode == MGlobalLogMode.logOutAndFullToSetupedErr) {
                            buffer.outBuffer.setLength(0);
                            buffer.fullBuffer.setLength(0);
                        }
                    }
                }

            });

        }

        /**
         * Override print methods to write to the thread-specific MStringBuilder.
         * Copyright Marco Scherzer, All rights reserved
         * Author Marco Scherzer
         *
         * @param s The string to print
         */
        @Override
        public void print(String s) {
            if (this == mout) getBufferForCurrentThread().outBuffer.append(s);
            else if (this == mfull) getBufferForCurrentThread().fullBuffer.append(s);
        }

        /**
         * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
         */
        @Override
        public void println(String s) {
            if (this == mout)
                getBufferForCurrentThread().outBuffer.append(s).append(System.lineSeparator());
            else if (this == mfull)
                getBufferForCurrentThread().fullBuffer.append(s).append(System.lineSeparator());
        }

        /**
         * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
         */
        @Override
        public void print(char c) {
            if (this == mout) getBufferForCurrentThread().outBuffer.append(c);
            else if (this == mfull) getBufferForCurrentThread().fullBuffer.append(c);
        }

        /**
         * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
         */
        @Override
        public void println(char c) {
            if (this == mout)
                getBufferForCurrentThread().outBuffer.append(c).append(System.lineSeparator());
            else if (this == mfull)
                getBufferForCurrentThread().fullBuffer.append(c).append(System.lineSeparator());
        }

        /**
         * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
         */
        @Override
        public void print(int i) {
            if (this == mout) getBufferForCurrentThread().outBuffer.append(i);
            else if (this == mfull) getBufferForCurrentThread().fullBuffer.append(i);
        }

        /**
         * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
         */
        @Override
        public void println(int i) {
            if (this == mout)
                getBufferForCurrentThread().outBuffer.append(i).append(System.lineSeparator());
            else if (this == mfull)
                getBufferForCurrentThread().fullBuffer.append(i).append(System.lineSeparator());
        }

        /**
         * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
         */
        @Override
        public void print(long l) {
            if (this == mout) getBufferForCurrentThread().outBuffer.append(l);
            else if (this == mfull) getBufferForCurrentThread().fullBuffer.append(l);
        }

        /**
         * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
         */
        @Override
        public void println(long l) {
            if (this == mout)
                getBufferForCurrentThread().outBuffer.append(l).append(System.lineSeparator());
            else if (this == mfull)
                getBufferForCurrentThread().fullBuffer.append(l).append(System.lineSeparator());
        }

        /**
         * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
         */
        @Override
        public void print(float f) {
            if (this == mout) getBufferForCurrentThread().outBuffer.append(f);
            else if (this == mfull) getBufferForCurrentThread().fullBuffer.append(f);
        }

        /**
         * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
         */
        @Override
        public void println(float f) {
            if (this == mout)
                getBufferForCurrentThread().outBuffer.append(f).append(System.lineSeparator());
            else if (this == mfull)
                getBufferForCurrentThread().fullBuffer.append(f).append(System.lineSeparator());
        }

        /**
         * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
         */
        @Override
        public void print(double d) {
            if (this == mout) getBufferForCurrentThread().outBuffer.append(d);
            else if (this == mfull) getBufferForCurrentThread().fullBuffer.append(d);
        }

        /**
         * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
         */
        @Override
        public void println(double d) {
            if (this == mout)
                getBufferForCurrentThread().outBuffer.append(d).append(System.lineSeparator());
            else if (this == mfull)
                getBufferForCurrentThread().fullBuffer.append(d).append(System.lineSeparator());
        }

        /**
         * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
         */
        @Override
        public void print(boolean b) {
            if (this == mout) getBufferForCurrentThread().outBuffer.append(b);
            else if (this == mfull) getBufferForCurrentThread().fullBuffer.append(b);
        }

        /**
         * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
         */
        @Override
        public void println(boolean b) {
            if (this == mout)
                getBufferForCurrentThread().outBuffer.append(b).append(System.lineSeparator());
            else if (this == mfull)
                getBufferForCurrentThread().fullBuffer.append(b).append(System.lineSeparator());
        }

        /**
         * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
         */
        @Override
        public void print(Object obj) {
            if (this == mout) getBufferForCurrentThread().outBuffer.append(obj);
            else if (this == mfull) getBufferForCurrentThread().fullBuffer.append(obj);
        }

        /**
         * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
         */
        @Override
        public void println(Object obj) {
            if (this == mout)
                getBufferForCurrentThread().outBuffer.append(obj).append(System.lineSeparator());
            else if (this == mfull)
                getBufferForCurrentThread().fullBuffer.append(obj).append(System.lineSeparator());
        }


    }
}




