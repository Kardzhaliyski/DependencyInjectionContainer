package com.github.kardzhaliyski.events;

import com.github.kardzhaliyski.annotations.Autowire;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executor;

public class SimpleApplicationEventMulticaster {
    private final ListenerStorage listenerStorage;
    private Executor taskExecutor = null;

    @Autowire
    public SimpleApplicationEventMulticaster(ListenerStorage listenerStorage) {
        this.listenerStorage = listenerStorage;
    }

    public void setTaskExecutor(Executor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void multicastEvent(ApplicationEvent event) {
        multicastEvent((Object) event);
    }

    private void multicastEvent(Object event) {
        for (ListenerInstance listener : listenerStorage.getListeners(event)) {
            Runnable task = createTask(listener, event);
            if (taskExecutor != null) {
                taskExecutor.execute(task);
            } else {
                task.run();
            }
        }
    }


    private Runnable createTask(ListenerInstance listener, Object event) {
        return new Runnable() {
            @Override
            public void run() {
                Object returnValue;
                try {
                    returnValue = listener.invoke(event);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                if (returnValue != null) {
                    multicastEvent(returnValue);
                }
            }
        };
    }


}
