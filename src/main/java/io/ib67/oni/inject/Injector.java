package io.ib67.oni.inject;

import io.ib67.oni.util.lang.ArrayUtil;
import io.ib67.oni.util.lang.Pair;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Simple Dependency Injector.
 *
 * @since 1.0
 */
public enum Injector {
    INSTANCE;
    private Map<Class<? extends Annotation>, List<Consumer<Pair<Object, Field>>>> fieldHandlers = new HashMap<>();
    private Map<Class<? extends Annotation>, List<Consumer<Pair<Object, Method>>>> methodHandlers = new HashMap<>();

    {
        fieldHandlers.put(ScanAll.class, new ArrayList<>());
        methodHandlers.put(ScanAll.class, new ArrayList<>());
    }

    public final Injector addFieldHandler(Class<? extends Annotation> clazz, Consumer<Pair<Object, Field>> handler) {
        if (fieldHandlers.containsKey(clazz)) {
            fieldHandlers.get(clazz).add(handler);
        } else {
            fieldHandlers.put(clazz, ArrayUtil.mutableList(handler));
        }
        return this;
    }

    public final Injector addMethodHandler(Class<? extends Annotation> clazz, Consumer<Pair<Object, Method>> handler) {
        if (methodHandlers.containsKey(clazz)) {
            methodHandlers.get(clazz).add(handler);
        } else {
            methodHandlers.put(clazz, ArrayUtil.mutableList(handler));
        }
        return this;
    }

    public final Injector handleAllMethod(Consumer<Pair<Object, Method>> handler) {
        return addMethodHandler(ScanAll.class, handler);
    }

    public final Injector handleAllField(Consumer<Pair<Object, Field>> handler) {
        return addFieldHandler(ScanAll.class, handler);
    }

    /**
     * Process a object.
     *
     * @param object target
     */
    public final void process(Object object) {
        Class<?> targetClass = object.getClass();
        for (Field field : targetClass.getDeclaredFields()) {
            fieldHandlers.get(ScanAll.class).forEach(h -> h.accept(Pair.of(object, field)));
            for (Annotation annotation : field.getAnnotations()) {
                if (fieldHandlers.containsKey(annotation.annotationType())) {
                    fieldHandlers.get(annotation.annotationType()).forEach(h -> h.accept(Pair.of(object, field)));
                }
            }
        }
        for (Method method : targetClass.getDeclaredMethods()) {
            methodHandlers.get(ScanAll.class).forEach(h -> h.accept(Pair.of(object, method)));
            for (Annotation annotation : method.getAnnotations()) {
                if (methodHandlers.containsKey(annotation.annotationType())) {
                    methodHandlers.get(annotation.annotationType()).forEach(h -> h.accept(Pair.of(object, method)));
                }
            }
        }
    }
}
