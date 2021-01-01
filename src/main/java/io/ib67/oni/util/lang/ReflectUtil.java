package io.ib67.oni.util.lang;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class ReflectUtil {
    /**
     * setAccessible,setField.
     */
    @SneakyThrows
    public static void setField(@NonNull Field field, Object object, Object... param) {
        field.setAccessible(true);
        field.set(object, param);
    }

    /**
     * setAccessible,getField
     */
    @SneakyThrows
    public static Object getField(@NonNull Field field, Object object) {
        field.setAccessible(true);
        return field.get(object);
    }
}
