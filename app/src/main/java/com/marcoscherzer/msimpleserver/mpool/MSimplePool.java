package com.marcoscherzer.msimpleserver.mpool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MSimplePool {
    private final ExecutorService executor;
    private final GCArrayList finishedTasks;
    private final AtomicInteger waiting = new AtomicInteger(0);
    private final AtomicInteger running = new AtomicInteger(0);
    private final AtomicInteger done = new AtomicInteger(0);

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MSimplePool() {
        this.executor = Executors.newCachedThreadPool();
        this.finishedTasks = new GCArrayList();
        this.finishedTasks.setGCMax(50);
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MSimplePool(ExecutorService executor) {
        this.executor = executor;
        this.finishedTasks = new GCArrayList();
        this.finishedTasks.setGCMax(50);
    }


    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public void submit(Supplier<? extends MJob<?>> mjobSupplier) {
        waiting.incrementAndGet();
        MOuterCallable<MJob> c = new MOuterCallable(mjobSupplier);
        executor.submit(c);
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public void shutdown() {
        executor.shutdown();
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public boolean isShutdown() {
        return executor.isShutdown();
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public int getMarkedForGcCount() {
        return finishedTasks.size();
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public int getWaitingTaskCount() {
        return waiting.get();
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public int getRunningTaskCount() {
        return running.get();
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public int getDoneTaskCount() {
        return done.get();
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static abstract class MJob<M> implements Callable<M> {
        private MHandler handler;

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        MHandler getHandler() {
            return handler;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public void setHandler(MHandler handler) {
            this.handler = handler;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public interface MHandler {
            /**
             * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
             */
            void handle(Object... params);
        }
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private class MOuterCallable<M> implements Callable<M> {
        private Supplier<MJob<M>> mjobSupplier;

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public MOuterCallable(Supplier<MJob<M>> mjobSupplier) {
            this.mjobSupplier = mjobSupplier;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        @Override
        public M call() throws Exception {
            MJob<M> job = mjobSupplier.get();
            mjobSupplier = null;
            running.incrementAndGet();
            waiting.decrementAndGet();
            job.call();
            if (job.getHandler() != null) job.getHandler().handle();

            running.decrementAndGet();
            finishedTasks.add(job);
            done.incrementAndGet();
            //finishedTasks.add(this);
            return null;
        }
    }


}
