package io.ib67.oni.inject;

import java.lang.annotation.*;

/**
 * OniInject, Could be used for DI.
 * The objects which annotated with will be processed automatically. (If not null)
 * (If null) fill the certain object. (DI)
 *
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface OniInject {

}
