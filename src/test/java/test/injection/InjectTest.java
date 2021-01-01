package test.injection;

import io.ib67.oni.inject.Injector;
import org.junit.Assert;
import org.junit.Test;

public class InjectTest {
    @TestField
    public String hello;

    @TestMethod
    public void onTrig(String a) {
        Assert.assertEquals(a, "Potato");
    }

    @Test
    public void Test() {
        long time = System.currentTimeMillis();
        Injector injector = Injector.INSTANCE;
        injector.addFieldHandler(TestField.class, p -> {
            try {
                p.value.set(p.key, "Not me");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        })
                .addFieldHandler(TestField.class, p -> {
                    try {
                        p.value.set(p.key, "Is me");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
        injector.addMethodHandler(TestMethod.class, p -> {
            try {
                p.value.invoke("Potato");
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
}
