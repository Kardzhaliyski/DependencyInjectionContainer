package com.github.kardzhaliyski.asyncs;

import net.bytebuddy.implementation.bind.annotation.Origin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.*;

public class AsyncInvocationHandler implements InvocationHandler {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Override
    public Object invoke(@Origin Object proxy, @Origin Method method, Object[] args) throws Throwable {
        Future<Object> future = executorService.submit(() -> method.invoke(proxy, args));
        if (Future.class.isAssignableFrom(method.getReturnType())) {
            return future;
//            return getFuture(future);
        } else if (method.getReturnType().equals(void.class)) {
            return null;
        }

        return null;//todo check if original throws
    }

    private Future<Object> getFuture(Future<Object> future) {
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
                return ((Future)future.get(timeout, unit)).get();
            }
        };
    }
}
