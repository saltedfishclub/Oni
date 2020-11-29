package io.ib67.oni.util.text;

import org.bukkit.ChatColor;

public class TextUtil {
    public static String translateColorChars(String msg){
        return ChatColor.translateAlternateColorCodes('&',msg);
    }
}
