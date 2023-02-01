package com.github.kardzhaliyski.events;

import java.util.*;

public class ListenerStorage {
    private final Set<ListenerInstance> listeners = new HashSet<>();
    private final Map<Class<?>, ListenerInstance[]> cache = new HashMap<>();
    public void addListener(ListenerInstance li) {
        if(li == null) {
            throw new IllegalArgumentException();
        }

        if(!cache.isEmpty()) {
            cache.clear();
        }

        listeners.add(li);
    }

    public ListenerInstance[] getListeners(Object event) {
        Class<?> eventType = event.getClass();
        ListenerInstance[] cachedListeners = cache.get(eventType);
        if(cachedListeners != null) {
            return cachedListeners;
        }

        List<ListenerInstance> list = new ArrayList<>();
        for (ListenerInstance listener : this.listeners) {
            if (!listener.type.isAssignableFrom(eventType)) {
                continue;
            }

            list.add(listener);
        }

        ListenerInstance[] arr = list.toArray(ListenerInstance[]::new);
        cache.put(eventType, arr);
        return arr;
    }
}
