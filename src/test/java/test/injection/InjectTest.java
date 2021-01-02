package test.injection;

import io.ib67.oni.inject.Injector;
import io.ib67.oni.inject.ProceedObject;
import io.ib67.oni.module.config.OniConfig;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;

public class InjectTest {
    @TestField
    public String hello;

    @TestMethod
    public void onTrig(String a) {
        Assert.assertEquals(a, "Potato");
    }

    @Test
    public void testNormal() {
        long time = System.currentTimeMillis();
        Injector injector = Injector.INSTANCE;
        injector.addFieldHandler(TestField.class, p -> {
            try {
                p.B.set(p.A, "Not me");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        })
                .addFieldHandler(TestField.class, p -> {
                    try {
                        p.B.set(p.A, "Is me");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
        injector.addMethodHandler(TestMethod.class, p -> {
            try {
                p.B.invoke("Potato");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
        injector.process(this);
        Assert.assertEquals(hello, "Is me");
        System.out.println(System.currentTimeMillis() - time + "ms");
    }

    @Test
    public void testObjectGraph() {
        ExampleConfig exampleConfig = new ExampleConfig();
        ProceedObject obj = Injector.INSTANCE.process(exampleConfig);
        obj.processAll();
        boolean triggered = false;
        for (Annotation annotation : obj.getAnnotations()) {
            if (annotation instanceof OniConfig) {
                OniConfig oc = (OniConfig) annotation;
                triggered = true;
                Assert.assertEquals(oc.value(), "conf.yml");
            }
        }
        Assert.assertTrue(triggered);
        Assert.assertTrue(obj.getSubObjects().stream().filter(o -> o.getProceedObject() instanceof ExampleConfig.NestedObj)
                .findFirst().get().getSubObjects().size() == 2);
    }
}
