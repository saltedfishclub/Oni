package io.ib67.oni.internal;

/**
 * Types annotated with this should be exported before leak into bukkit.
 * **Mention:** A typical Exportable type will export the WHOLE native object.
 *
 * @param <T>
 * @see io.ib67.oni.util.annotation.ExportRequired
 */
public interface Exportable<T> {
    T export();
}
