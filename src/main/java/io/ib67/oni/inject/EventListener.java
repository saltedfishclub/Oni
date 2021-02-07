package io.ib67.oni.inject;

/**
 * Auto-registers event handlers
 */
public @interface EventListener {
    boolean autoInitial() default false;
}
