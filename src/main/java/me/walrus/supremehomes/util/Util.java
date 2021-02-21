package me.walrus.supremehomes.util;

import me.walrus.supremehomes.SupremeHomes;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Util {


    public static String colorText(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendMessage(Player player, String message){
        player.sendMessage(colorText(SupremeHomes.getConfigManager().getPrefix() + message));
    }
}
