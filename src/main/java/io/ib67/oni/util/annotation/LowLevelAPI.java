package io.ib67.oni.util.annotation;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Low level api, not recommended for use.
 */
@Retention(RetentionPolicy.SOURCE)
@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, TYPE})
@ApiStatus.Internal
public @interface LowLevelAPI {
}
