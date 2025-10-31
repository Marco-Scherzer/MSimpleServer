package com.marcoscherzer.msimpleserver.mpool;

import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.mout;

import com.marcoscherzer.msimpleserver.mpool.MSimplePool.MJob;

import java.util.function.Supplier;

/**
 * Author: Marco Scherzer
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, Architectures & Copyright Marco Scherzer 2017, All rights reserved, base-principle reimplementation 2025. All Rights Reserved.
 */
class Main {
    public static void main_(String[] args) {
        MSimplePool pool = new MSimplePool();
        // Test 1:Supplier memtest. Submit suppliers for 3 minutes
        long endTime = System.currentTimeMillis() + 3 * 60 * 1000;
        while (System.currentTimeMillis() < endTime) {
            pool.submit(new MT1());
            // Print metrics periodically
            //if (System.currentTimeMillis() % 2000 == 0) {
            mout.println("Metrics:");
            mout.println("Waiting tasks: " + pool.getWaitingTaskCount());
            mout.println("Running tasks: " + pool.getRunningTaskCount());
            mout.println("Done tasks: " + pool.getDoneTaskCount());
            //}
        }

// Test 2:
        int maxQueueSize = 500;  // Maximum number of tasks allowed in the waiting queue

        while (System.currentTimeMillis() < endTime) {
            if (pool.getWaitingTaskCount() < maxQueueSize) {
                mout.println("submitting");
                pool.submit(new MT1());
            }

            // Print metrics periodically
            //if (System.currentTimeMillis() % 2000 == 0) {
            mout.println("Metrics:");
            mout.println("Waiting tasks: " + pool.getWaitingTaskCount());
            mout.println("Running tasks: " + pool.getRunningTaskCount());
            mout.println("Done tasks: " + pool.getDoneTaskCount());
            //}
        }

        pool.shutdown();
    }

    static final class MT1 implements Supplier {
        static int i;

        @Override
        public Object get() {
            return new MJob() {

                @Override
                public Object call() {
                    // Simulate job execution
                    try {
                        mout.println(i++);
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return null;
                }
            };
        }
    }
}