package test.injection;

import io.ib67.oni.module.config.Comment;
import io.ib67.oni.module.config.OniConfig;

@OniConfig("conf.yml")
public class ExampleConfig {
    @Comment({"Hello", "This is a example comment."})
    public String field = "a";
    public String fieldNull;
    @Comment("int field.")
    public int intField = 0;
    public NestedObj nestedObj = new NestedObj();

    public static class NestedObj {
        @Comment("aaaaa")
        public String nestedString = "p";
        public String nestedStringNull;
    }
}
