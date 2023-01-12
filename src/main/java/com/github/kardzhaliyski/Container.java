package com.github.kardzhaliyski;

import com.github.kardzhaliyski.annotations.Default;
import com.github.kardzhaliyski.annotations.Inject;
import com.github.kardzhaliyski.annotations.Named;
import com.github.kardzhaliyski.annotations.NamedParameter;
import com.github.kardzhaliyski.classes.Initializer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.*;

public class Container {
    private final Map<String, Object> namedInstances = new HashMap<>();
    private final Map<Class<?>, Object> classInstances = new HashMap<>();
    private final Map<Class<?>, Class<?>> implementations = new HashMap<>();
    private final Set<Class<?>> initsInProgress = new HashSet<>();

    public Container() {
    }

    public Container(Properties properties) {
        properties.forEach((n, v) -> namedInstances.put(n.toString(), v));
    }

    public Object getInstance(String key) {
        return namedInstances.get(key);
    }

    public <T> T getInstance(Class<T> c) throws Exception {
        Object ins = classInstances.get(c);
        if (ins != null) {
            return (T) ins;
        }

        Class<?> clazzImpl = null;
        if (c.isInterface()) {
            clazzImpl = implementations.get(c);
            if (clazzImpl == null) {
                Default ann = c.getDeclaredAnnotation(Default.class);
                if (ann != null) {
                    clazzImpl = ann.value();
                    implementations.put(c, clazzImpl);
                }
            }

            if (clazzImpl == null) {
                return null; //todo throws
            }
        }

        clazzImpl = clazzImpl != null ? clazzImpl : c;
        Object instance = classInstances.get(clazzImpl);
        if (instance != null) {
            return (T) instance;
        }

        if (initsInProgress.contains(clazzImpl)) {
            throw new IllegalStateException("Circular dependancy error. For class: " + clazzImpl);
        }

        try {
            initsInProgress.add(clazzImpl);
            ins = (T) initClass(clazzImpl);
        } finally {
            initsInProgress.remove(clazzImpl);
        }
        classInstances.put(c, ins);
        return (T) ins;
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
        Parameter[] consParams = constructor.getParameters();
        if (consParams.length != 0) {
            params = new Object[consParams.length];
        }

        for (int i = 0; i < consParams.length; i++) {
            Parameter param = consParams[i];
            NamedParameter named = param.getAnnotation(NamedParameter.class);
            if (named != null) {
                params[i] = getInstance(named.value());
                continue;
            }

            Class<?> pt = param.getType();
            params[i] = getInstance(pt); //todo maybe cache exception
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
        classInstances.put(c, instance); //todo throws if c exists
    }

    public void registerInstance(Object instance) {
        registerInstance(instance.getClass(), instance);
    }
}
