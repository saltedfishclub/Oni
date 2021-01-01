package io.ib67.oni.inject;

import io.ib67.oni.util.lang.ArrayUtil;
import io.ib67.oni.util.lang.Triple;

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
    private Map<Class<? extends Annotation>, List<Consumer<Triple<Object, Field, Annotation>>>> fieldHandlers = new HashMap<>();
    private Map<Class<? extends Annotation>, List<Consumer<Triple<Object, Method, Annotation>>>> methodHandlers = new HashMap<>();

    {
        fieldHandlers.put(ScanAll.class, new ArrayList<>());
        methodHandlers.put(ScanAll.class, new ArrayList<>());
    }

    /**
     * Add a field handler,Triggered when object was processed.
     *
     * @param clazz   target class
     * @param handler ur handler
     * @return Injector for fluent api.
     * @since 1.0
     */
    public final Injector addFieldHandler(Class<? extends Annotation> clazz, Consumer<Triple<Object, Field, Annotation>> handler) {
        if (fieldHandlers.containsKey(clazz)) {
            fieldHandlers.get(clazz).add(handler);
        } else {
            fieldHandlers.put(clazz, ArrayUtil.mutableList(handler));
        }
        return this;
    }

    /**
     * Add a method handler,Triggered when object was processed.
     *
     * @param clazz   target class
     * @param handler ur handler
     * @return Injector for fluent api.
     * @since 1.0
     */
    public final Injector addMethodHandler(Class<? extends Annotation> clazz, Consumer<Triple<Object, Method, Annotation>> handler) {
        if (methodHandlers.containsKey(clazz)) {
            methodHandlers.get(clazz).add(handler);
        } else {
            methodHandlers.put(clazz, ArrayUtil.mutableList(handler));
        }
        return this;
    }

    /**
     * Add a global field handler,Triggered when object was processed.
     *
     * @param handler ur handler
     * @return Injector for fluent api.
     * @since 1.0
     */
    public final Injector handleAllMethod(Consumer<Triple<Object, Method, Annotation>> handler) {
        return addMethodHandler(ScanAll.class, handler);
    }

    /**
     * Add a global method handler,Triggered when object was processed.
     *
     * @param handler ur handler
     * @return Injector for fluent api.
     * @since 1.0
     */

    public final Injector handleAllField(Consumer<Triple<Object, Field, Annotation>> handler) {
        return addFieldHandler(ScanAll.class, handler);
    }

    /**
     * Process a object.
     *
     * @param object target
     * @since 1.0
     */
    public final void process(Object object) {
        Class<?> targetClass = object.getClass();
        for (Field field : targetClass.getDeclaredFields()) {
            fieldHandlers.get(ScanAll.class).forEach(h -> h.accept(Triple.of(object, field,null)));
            for (Annotation annotation : field.getAnnotations()) {
                if (fieldHandlers.containsKey(annotation.annotationType())) {
                    fieldHandlers.get(annotation.annotationType()).forEach(h -> h.accept(Triple.of(object, field,annotation)));
                }
            }
        }
        for (Method method : targetClass.getDeclaredMethods()) {
            methodHandlers.get(ScanAll.class).forEach(h -> h.accept(Triple.of(object, method,null)));
            for (Annotation annotation : method.getAnnotations()) {
                if (methodHandlers.containsKey(annotation.annotationType())) {
                    methodHandlers.get(annotation.annotationType()).forEach(h -> h.accept(Triple.of(object, method,annotation)));
                }
            }
        }
    }
}
