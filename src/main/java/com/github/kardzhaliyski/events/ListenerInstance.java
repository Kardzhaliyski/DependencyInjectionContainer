package com.github.kardzhaliyski.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ListenerInstance {
    public Object instance;
    public Method method;
    public Class<?> type;

    public ListenerInstance(Object instance, Method method) {
        if (instance == null || method == null) {
            throw new IllegalArgumentException();
        }

        if (method.getParameterCount() != 1) {
            throw new IllegalArgumentException();
        }

        this.instance = instance;
        this.method = method;
        this.method.setAccessible(true);
        this.type = method.getParameterTypes()[0];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListenerInstance that = (ListenerInstance) o;

        if (!instance.equals(that.instance)) return false;
        return method.equals(that.method);
    }

    public Object invoke(Object arg) throws InvocationTargetException, IllegalAccessException {
        return this.method.invoke(instance, arg);
    }
}
