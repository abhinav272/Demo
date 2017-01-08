package com.android.shopr.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by abhinav.sharma on 1/7/2017.
 */

public class ExecutorSupplier {

    private static ExecutorSupplier executorSupplier;
    private final ThreadPoolExecutor workerThreadExecutor;
    private final MainThreadExecutor mainThreadExecutor;

    private ExecutorSupplier() {
        mainThreadExecutor = new MainThreadExecutor();
        workerThreadExecutor = new ThreadPoolExecutor(2, 2, 30L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
    }

    public ExecutorSupplier getInstance() {
        if (executorSupplier == null) {
            executorSupplier = new ExecutorSupplier();
        }

        return executorSupplier;
    }

    public Executor getMainThreadExecutor() {
        return mainThreadExecutor;
    }

    public ThreadPoolExecutor getWorkerThreadExecutor() {
        return workerThreadExecutor;
    }
}
