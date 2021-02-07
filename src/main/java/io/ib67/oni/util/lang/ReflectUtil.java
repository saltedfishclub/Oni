package io.ib67.oni.util.lang;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectUtil {
    /**
     * setAccessible,setField.
     *
     * @since 1.0
     */
    @SneakyThrows
    public static void setField(@NonNull Field field, Object object, Object... param) {
        field.setAccessible(true);
        field.set(object, param);
    }

    /**
     * setAccessible,getField
     *
     * @since 1.0
     */
    @SneakyThrows
    public static Object getField(@NonNull Field field, Object object) {
        field.setAccessible(true);
        return field.get(object);
    }

    @SneakyThrows
    public static Object invokeMethod(@NonNull Method method, Object object, Object... params) {
        method.setAccessible(true);
        return method.invoke(object, params);
    }

    @SneakyThrows
    public static Method methodOf(Class<?> target, String name, Class<?>... params) {
        return target.getMethod(name, params);
    }

    public static boolean containsAnnotation(List<Annotation> annotations, Class<? extends Annotation> annotation) {
        for (Annotation a : annotations) {
            if (a.annotationType().getCanonicalName().equals(annotation.getCanonicalName())) {
                return true;
            }
        }
        return false;
    }
}
