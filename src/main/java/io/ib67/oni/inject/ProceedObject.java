package io.ib67.oni.inject;

import io.ib67.oni.util.annotation.LowLevelAPI;
import io.ib67.oni.util.lang.ReflectUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Field graph of a object.
 * **NOT RECOMMENDED**: Its hard to use
 *
 * @since 1.0
 */
@LowLevelAPI
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter(AccessLevel.PACKAGE)
public class ProceedObject {
    /**
     * The top-level processed object
     */
    private final Object proceedObject;
    /**
     * isProceed
     */
    private boolean proceed;
    private boolean noinject;
    /**
     * Annotations with the proceedObject
     */
    private List<Annotation> annotations = new ArrayList<>();
    /**
     * The fields of the proceedObject
     */
    private List<ProceedObject> subObjects = new ArrayList<>();

    public ProceedObject processAll() {
        if (!proceed) { //process itself
            process();
        }   // if not proceed (up)
        if (!noinject) {
            subObjects.forEach(ProceedObject::processAll); //process subs.
        }
        return this;
    }

    private ProceedObject process() {
        proceed = true;
        if (ReflectUtil.containsAnnotation(annotations, NoInject.class)) {
            noinject = true;
            return this;
        }
        if (proceedObject == null) {
            return this;
        }
        if (proceedObject.getClass().getCanonicalName().startsWith("java")) {
            noinject = true;
            return this;
        }
        ProceedObject po = Injector.INSTANCE.process(proceedObject);
        this.annotations = po.annotations;
        this.subObjects = po.subObjects;
        return this;
    }
}
