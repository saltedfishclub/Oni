package io.ib67.oni.inject;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation will not make any effect.
 * Just made for Injector.handleAllField/Method.
 * Don't annotate anything with it.
 *
 * @since 1.0
 */
@ApiStatus.Internal
@Target(ElementType.ANNOTATION_TYPE)
public @interface ScanAll {
}
