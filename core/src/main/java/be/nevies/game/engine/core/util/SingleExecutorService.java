/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.core.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This service allows only one task to be run at any time. If you submit tasks while there's still a task running, the new one we'll be discarded.
 * This service doesn't work with Task and Service from JavaFX!!!!
 * 
 * @author drs
 */
public class SingleExecutorService extends ThreadPoolExecutor {

    /* Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(SingleExecutorService.class);
    private RunnableFuture<?> currentTask;
    private Future<?> currentCall;

    public SingleExecutorService() {
        super(1, 1, Long.MAX_VALUE, TimeUnit.NANOSECONDS, new LinkedBlockingQueue<Runnable>(1));
    }

    @Override
    public Future<?> submit(Runnable task) {
        if (task == null) {
            throw new NullPointerException();
        }
        synchronized (this) {
            if (currentTask == null || currentTask.isDone() || currentTask.isCancelled()) {
                currentTask = newTaskFor(task, null);
                execute(currentTask);
                return currentTask;
            } else {
                LOG.warn("The Task wasn't submit because there is still a task running. Current : '{}' , Submitted : '{}'.", currentTask, task);
            }
        }
        return null;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        if (task == null) {
            throw new NullPointerException();
        }
        synchronized (this) {
            if (currentCall == null || currentCall.isDone() || currentCall.isCancelled()) {
                currentCall = super.submit(task);
                return (Future<T>) currentCall;
            } else {
                LOG.warn("The Task wasn't submit because there is still a task running. Current : '{}' , Submitted : '{}'.", currentTask, task);
            }
        }
        return null;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        super.submit(task, result);
        if (task == null) {
            throw new NullPointerException();
        }
        synchronized (this) {
            if (currentTask == null || currentTask.isDone() || currentTask.isCancelled()) {
                currentTask = newTaskFor(task, result);
                execute(currentTask);
                return (Future<T>) currentTask;
            } else {
                LOG.warn("The Task wasn't submit because there is still a task running. Current : '{}' , Submitted : '{}'.", currentTask, task);
            }
        }
        return null;
    }
}
