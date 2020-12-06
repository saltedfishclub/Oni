package io.ib67.oni.util;

import org.bukkit.Bukkit;
import sun.misc.Unsafe;

import java.io.File;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;


/**
 * @author sky
 * @since 2020-04-12 22:39
 */
public class Loader {

    static MethodHandles.Lookup lookup;
    static Unsafe UNSAFE;
    static Method ADD_URL_METHOD;

    static {
        try {
            ADD_URL_METHOD = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            ADD_URL_METHOD.setAccessible(true);
        } catch (Throwable ignore) {
        }
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (Unsafe) field.get(null);
            Field lookupField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            Object lookupBase = UNSAFE.staticFieldBase(lookupField);
            long lookupOffset = UNSAFE.staticFieldOffset(lookupField);
            lookup = (MethodHandles.Lookup) UNSAFE.getObject(lookupBase, lookupOffset);
        } catch (Throwable ignore) {
        }
    }


    /**
     * 将文件读取至内存中
     * 读取后不会随着插件的卸载而卸载
     * 请在执行前判断是否已经被读取
     * 防止出现未知错误
     */
    public static void addPath(File file) {
        try {
            ClassLoader loader = Bukkit.class.getClassLoader();
            if ("LaunchClassLoader".equals(loader.getClass().getSimpleName())) {
                MethodHandle methodHandle = lookup.findVirtual(loader.getClass(), "addURL", MethodType.methodType(void.class, java.net.URL.class));
                methodHandle.invoke(loader, file.toURI().toURL());
            } else {
                Field ucpField = loader.getClass().getDeclaredField("ucp");
                long ucpOffset = UNSAFE.objectFieldOffset(ucpField);
                Object ucp = UNSAFE.getObject(loader, ucpOffset);
                MethodHandle methodHandle = lookup.findVirtual(ucp.getClass(), "addURL", MethodType.methodType(void.class, java.net.URL.class));
                methodHandle.invoke(ucp, file.toURI().toURL());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static Class<?> forName(String name, boolean initialize, ClassLoader loader) {
        try {
            return Class.forName(name, initialize, loader);
        } catch (Throwable ignored) {
            return null;
        }
    }
}
