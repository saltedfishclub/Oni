package io.ib67.oni.util.lang;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayUtil {
    public static boolean isEmptyOrNull(Object[] array) {
        return array == null || array.length == 0;
    }

    public static <T> ArrayList<T> mutableList(T... args) {
        ArrayList<T> list = new ArrayList<>();
        if (isEmptyOrNull(args)) {
            return list;
        }
        list.addAll(Arrays.asList(args));
        return list;
    }
}
