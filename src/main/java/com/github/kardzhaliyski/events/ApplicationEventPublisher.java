package com.github.kardzhaliyski.events;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class ApplicationEventPublisher {
    private Set<ListenerInstance> listeners;

    public ApplicationEventPublisher() {
        this.listeners = new HashSet<>();
    }

    public void addListener(ListenerInstance li) {
        if(li == null) {
            throw new IllegalArgumentException();
        }

        listeners.add(li);
    }

    public void publishEvent(Object object) {
        for (ListenerInstance listener : listeners) {
            if (!listener.type.isAssignableFrom(object.getClass())) {
                continue;
            }

            Object returnValue;
            try {
                returnValue = listener.invoke(object);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            if(returnValue != null) {
                publishEvent(returnValue);
            }
        }
    }

    public void publishEvent(ApplicationEvent event) {
        publishEvent((Object) event);
    }

}
