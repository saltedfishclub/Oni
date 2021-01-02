package io.ib67.oni.module.config;

import java.lang.annotation.*;

/**
 * Generates config comment automatically.
 * use it with {@link OniConfig}
 *
 * @since 1.0
 */
@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Comment {
    String[] value();
}
