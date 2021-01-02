package io.ib67.oni.module.config;

import java.lang.annotation.*;

/**
 * Auto-load config annotation.
 *
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OniConfig {
    String value() default "config.yml";
}
