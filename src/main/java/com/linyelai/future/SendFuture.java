package com.linyelai.future;

import java.util.concurrent.*;

public class SendFuture<V> implements Future<V> {
    private volatile V result;
    private volatile ExecutionException exception;
    private volatile boolean cancelled;
    private volatile boolean done;
    private final CountDownLatch latch = new CountDownLatch(1);

    public void set(V result) {
        this.result = result;
        done = true;
        latch.countDown();
    }

    public void setException(Throwable throwable) {
        this.exception = new ExecutionException(throwable);
        done = true;
        latch.countDown();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (done) {
            return false;
        }
        cancelled = true;
        done = true;
        latch.countDown();
        return true;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        latch.await();
        if (cancelled) {
            throw new CancellationException();
        }
        if (exception != null) {
            throw exception;
        }
        return result;
    }

    @Override
    public V get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        if (!latch.await(timeout, unit)) {
            throw new TimeoutException();
        }
        if (cancelled) {
            throw new CancellationException();
        }
        if (exception != null) {
            throw exception;
        }
        return result;
    }
}