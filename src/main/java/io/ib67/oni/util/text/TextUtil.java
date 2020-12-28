package io.ib67.oni.util.text;


import org.bukkit.ChatColor;

/**
 * Tools about Strings
 *
 * @since 1.0
 */
public class TextUtil {
    /**
     * <p>Translate Color Char ("and" char)
     *
     * @param msg target message
     * @return translated message
     */
    public static String translateColorChars(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
