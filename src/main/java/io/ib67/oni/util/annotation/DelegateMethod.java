package io.ib67.oni.util.annotation;

import java.lang.annotation.*;

/**
 * A method that delegated to other method.
 * Method signature(expect: name) keep sync with the target method.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface DelegateMethod {
    Class<?> value();
}
