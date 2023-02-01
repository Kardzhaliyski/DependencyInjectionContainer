package com.github.kardzhaliyski.events;

import com.github.kardzhaliyski.annotations.Autowire;

import java.lang.reflect.InvocationTargetException;

public class ApplicationEventPublisher {
    private final ListenerStorage listenerStorage;

    @Autowire
    public ApplicationEventPublisher(ListenerStorage listenerStorage) {
        this.listenerStorage = listenerStorage;
    }

    public void publishEvent(Object object) {
        for (ListenerInstance listener : listenerStorage.getListeners(object)) {
            Object returnValue;
            try {
                returnValue = listener.invoke(object);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            if (returnValue != null) {
                publishEvent(returnValue);
            }
        }
    }

    public void publishEvent(ApplicationEvent event) {
        publishEvent((Object) event);
    }

}
