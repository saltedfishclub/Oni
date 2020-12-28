package io.ib67.oni.util.annotation;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.*;

/**
 * Types with this annotation should be exported before object leak into bukkit.
 *
 * @see io.ib67.oni.internal.Exportable
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@ApiStatus.Internal
public @interface ExportRequired {
}
