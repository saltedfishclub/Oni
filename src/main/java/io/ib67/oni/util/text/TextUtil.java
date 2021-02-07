package io.ib67.oni.util.text;


import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.bukkit.ChatColor;

/**
 * Tools about Strings
 *
 * @since 1.0
 */
public class TextUtil {
    public static JsonParser jsonParser = new JsonParser();
    public static Gson gson = new Gson();

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
