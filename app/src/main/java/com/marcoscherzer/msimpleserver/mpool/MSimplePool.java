package com.marcoscherzer.msimpleserver.mpool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


/**
 * Author: Marco Scherzer
 *
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, Architectures & Copyright Marco Scherzer 2017, All rights reserved, base-principle reimplementation 2025. All Rights Reserved.
 */
public class MSimplePool {
    private final ExecutorService executor;
    private final GCArrayList finishedTasks;
    private final AtomicInteger waiting = new AtomicInteger(0);
    private final AtomicInteger running = new AtomicInteger(0);
    private final AtomicInteger done = new AtomicInteger(0);

    /**
     * Author: Marco Scherzer
     *
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, Architectures & Copyright Marco Scherzer 2017, All rights reserved, base-principle reimplementation 2025. All Rights Reserved.
     */
    public MSimplePool() {
        this.executor = Executors.newCachedThreadPool();
        this.finishedTasks = new GCArrayList();
        this.finishedTasks.setGCMax(50);
    }

    /**
     * Author: Marco Scherzer
     *
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, Architectures & Copyright Marco Scherzer 2017, All rights reserved, base-principle reimplementation 2025. All Rights Reserved.
     */
    public MSimplePool(ExecutorService executor) {
        this.executor = executor;
        this.finishedTasks = new GCArrayList();
        this.finishedTasks.setGCMax(50);
    }


    /**
     * Author: Marco Scherzer
     *
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, Architectures & Copyright Marco Scherzer 2017, All rights reserved, base-principle reimplementation 2025. All Rights Reserved.
     */
    public void submit(Supplier<? extends MJob<?>> mjobSupplier) {
        waiting.incrementAndGet();
        MOuterCallable<MJob> c = new MOuterCallable(mjobSupplier);
        executor.submit(c);
    }

    /**
     * Author: Marco Scherzer
     *
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, Architectures & Copyright Marco Scherzer 2017, All rights reserved, base-principle reimplementation 2025. All Rights Reserved.
     */
    public void shutdown() {
        executor.shutdown();
    }

    /**
     * Author: Marco Scherzer
     *
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, Architectures & Copyright Marco Scherzer 2017, All rights reserved, base-principle reimplementation 2025. All Rights Reserved.
     */
    public boolean isShutdown() {
        return executor.isShutdown();
    }

    /**
     * Author: Marco Scherzer
     *
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, Architectures & Copyright Marco Scherzer 2017, All rights reserved, base-principle reimplementation 2025. All Rights Reserved.
     */
    public int getMarkedForGcCount() {
        return finishedTasks.size();
    }

    /**
     * Author: Marco Scherzer
     *
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, Architectures & Copyright Marco Scherzer 2017, All rights reserved, base-principle reimplementation 2025. All Rights Reserved.
     */
    public int getWaitingTaskCount() {
        return waiting.get();
    }

    /**
     * Author: Marco Scherzer
     *
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, Architectures & Copyright Marco Scherzer 2017, All rights reserved, base-principle reimplementation 2025. All Rights Reserved.
     */
    public int getRunningTaskCount() {
        return running.get();
    }

    /**
     * Author: Marco Scherzer
     *
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, Architectures & Copyright Marco Scherzer 2017, All rights reserved, base-principle reimplementation 2025. All Rights Reserved.
     */
    public int getDoneTaskCount() {
        return done.get();
    }

    /**
     * Author: Marco Scherzer
     *
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, Architectures & Copyright Marco Scherzer 2017, All rights reserved, base-principle reimplementation 2025. All Rights Reserved.
     */
    public static abstract class MJob<M> implements Callable<M> {
        private MHandler handler;

        MHandler getHandler() {
            return handler;
        }

        public void setHandler(MHandler handler) {
            this.handler = handler;
        }

        public interface MHandler {
            void handle(Object... params);
        }
    }

    /**
     * Author: Marco Scherzer
     *
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, Architectures & Copyright Marco Scherzer 2017, All rights reserved, base-principle reimplementation 2025. All Rights Reserved.
     */
    private class MOuterCallable<M> implements Callable<M> {
        private Supplier<MJob<M>> mjobSupplier;

        public MOuterCallable(Supplier<MJob<M>> mjobSupplier) {
            this.mjobSupplier = mjobSupplier;
        }

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
