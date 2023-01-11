package com.github.kardzhaliyski;

import com.github.kardzhaliyski.annotations.Default;
import com.github.kardzhaliyski.annotations.Inject;
import com.github.kardzhaliyski.annotations.Named;
import com.github.kardzhaliyski.classes.Initializer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Container {
    private final Map<String, Object> namedInstances = new HashMap<>();
    private final Map<Class<?>, Object> classInstances = new HashMap<>();
    private final Map<Class<?>, Class<?>> implementations = new HashMap<>();

    public Object getInstance(String key) {
        return namedInstances.get(key);
    }

    public <T> T getInstance(Class<T> c) throws Exception {

        Class<?> clazz = implementations.get(c);
        if (clazz == null) {
            Default ann = c.getDeclaredAnnotation(Default.class);
            if (ann != null) {
                clazz = ann.value();
                implementations.put(c, clazz);
            }
        }

        if (clazz == null && c.isInterface()) {
            return null;
        }

        clazz = clazz != null ? clazz : c;
        Object instance = classInstances.get(clazz);
        if (instance != null) {
            return (T) instance;
        }

        return (T) initClass(clazz);
    }

    private Object initClass(Class<?> clazz) throws Exception {
        Object instance = makeInstance(clazz);
        injectFields(instance);

        if (Initializer.class.isAssignableFrom(clazz)) {
            ((Initializer) instance).init();
        }

        classInstances.put(clazz, instance);
        return instance;
    }

    private void injectFields(Object instance) throws Exception {
        Class<?> clazz = instance.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            Inject ann = field.getDeclaredAnnotation(Inject.class);
            if (ann == null) {
                continue;
            }

            Named named = field.getDeclaredAnnotation(Named.class);
            Object o;
            if (named != null) {
                o = getInstance(field.getName());
            } else {
                Class<?> type = field.getType();
                o = getInstance(type);
            }

            field.set(instance, o);
        }
    }

    private Object makeInstance(Class<?> clazz) throws Exception {
        Constructor<?> constructor = getConstructor(clazz);
        Object[] params = getParams(constructor);
        return constructor.newInstance(params);
    }

    private Object[] getParams(Constructor<?> constructor) throws Exception {
        Object[] params = null;
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        if (parameterTypes.length != 0) {
            params = new Object[parameterTypes.length];
        }

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> pt = parameterTypes[i];
            Object parIns = getInstance(pt); //todo maybe cache exception
            params[i] = parIns;
        }
        return params;
    }

    private static Constructor<?> getConstructor(Class<?> clazz) throws NoSuchMethodException {
        Constructor<?> constructor = null;
        for (Constructor<?> c : clazz.getDeclaredConstructors()) {
            Inject ann = c.getDeclaredAnnotation(Inject.class);
            if (ann == null) {
                continue;
            }

            c.setAccessible(true);
            constructor = c;
            break; //todo maybe check if another constructor with Inject is found and throw
        }

        constructor = constructor != null ? constructor : clazz.getConstructor();
        return constructor;
    }

    public void decorateInstance(Object o) throws Exception {
        injectFields(o);
    }

    public void registerInstance(String key, Object instance) {
        namedInstances.put(key, instance);
    }

    public <T> void registerImplementation(Class<T> c, Class<? extends T> subClass) {
        implementations.put(c, subClass);
    }

    public void registerInstance(Class c, Object instance) {
        classInstances.put(c, instance);
    }

    public void registerInstance(Object instance) {
        registerInstance(instance.getClass(), instance);
    }
}
