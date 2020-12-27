package io.ib67.oni;

import io.ib67.oni.util.annotation.LowLevelAPI;
import io.ib67.oni.util.text.TextUtil;

/**
 * Those text prefixes
 *
 * @since 1.0
 */
@LowLevelAPI
public class ConstPrefix {
    public static String WARN = TextUtil.translateColorChars("&c&l");
    public static String FATAL = TextUtil.translateColorChars("&4&l");
    public static String TIP = TextUtil.translateColorChars("&a&l");

}
