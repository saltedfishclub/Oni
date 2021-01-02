package io.ib67.oni.inject;

import io.ib67.oni.util.lang.ArrayUtil;
import io.ib67.oni.util.lang.Pair;
import io.ib67.oni.util.lang.ReflectUtil;
import io.ib67.oni.util.lang.Triple;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
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
    public final Injector addFieldHandler(@NonNull Class<? extends Annotation> clazz, @NonNull Consumer<Triple<Object, Field, Annotation>> handler) {
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
    public final Injector addMethodHandler(@NonNull Class<? extends Annotation> clazz, @NonNull Consumer<Triple<Object, Method, Annotation>> handler) {
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
    public final Injector handleAllMethod(@NonNull Consumer<Triple<Object, Method, Annotation>> handler) {
        return addMethodHandler(ScanAll.class, handler);
    }

    /**
     * Add a global method handler,Triggered when object was processed.
     *
     * @param handler ur handler
     * @return Injector for fluent api.
     * @since 1.0
     */
    public final Injector handleAllField(@NonNull Consumer<Triple<Object, Field, Annotation>> handler) {
        return addFieldHandler(ScanAll.class, handler);
    }

    /**
     * Handle all field of specified type
     *
     * @param type    type
     * @param handler handler
     * @return Injector for fluent api
     * @since 1.0
     */
    public final Injector handleFieldType(@NonNull Class<?> type, Consumer<Pair<Object, Field>> handler) {
        return handleAllField(t -> {
            if (t.B.getType().getCanonicalName().equals(type.getCanonicalName())) {
                handler.accept(Pair.of(t.A, t.B));
            }
        });
    }

    /**
     * Process a object.
     *
     * @param object target
     * @since 1.0
     */
    public final ProceedObject process(@NonNull Object object) {
        Class<?> targetClass = object.getClass();
        ProceedObject graph = new ProceedObject(object);
        graph.setProceed(true);
        graph.setAnnotations(Arrays.asList(targetClass.getAnnotations()));

        for (Field field : targetClass.getDeclaredFields()) {
            fieldHandlers.get(ScanAll.class).forEach(h -> h.accept(Triple.of(object, field, null)));
            ProceedObject obj = new ProceedObject(ReflectUtil.getField(field, object));
            obj.setAnnotations(Arrays.asList(field.getAnnotations()));
            graph.getSubObjects().add(obj);
            for (Annotation annotation : field.getAnnotations()) {
                if (fieldHandlers.containsKey(annotation.annotationType())) {
                    fieldHandlers.get(annotation.annotationType()).forEach(h -> h.accept(Triple.of(object, field, annotation)));
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
        return graph;
    }
}
