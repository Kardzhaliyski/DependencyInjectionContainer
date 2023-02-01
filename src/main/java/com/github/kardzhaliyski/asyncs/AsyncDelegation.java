package com.github.kardzhaliyski.asyncs;

import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.concurrent.*;

public class AsyncDelegation {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static Future<Object> asyncInvoke(@SuperCall Callable callable) throws Exception {
        Future<Object> future = executorService.submit(callable);
        return wrap(future);
    }

    private static Future<Object> wrap(Future<Object> future) {
        return new Future<Object>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return future.cancel(mayInterruptIfRunning);
            }

            @Override
            public boolean isCancelled() {
                return future.isCancelled();
            }

            @Override
            public boolean isDone() {
                return future.isDone();
            }

            @Override
            public Object get() throws InterruptedException, ExecutionException {
                System.out.println("In 1");
                Future future1 = (Future) future.get();
                System.out.println("In 2");
                return future1.get();
            }

            @Override
            public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return ((Future) future.get(timeout, unit)).get();
            }
        };
    }
}
