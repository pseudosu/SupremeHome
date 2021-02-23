package me.walrus.supremehomes.listeners;

import me.walrus.supremehomes.network.DatabaseManager;
import me.walrus.supremehomes.network.UpdateChecker;
import me.walrus.supremehomes.util.Permissions;
import me.walrus.supremehomes.util.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class JoinListener implements Listener {

    private Plugin plugin;

    public JoinListener(Plugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if(event.getPlayer().hasPermission(Permissions.ADMIN)){
            new UpdateChecker((JavaPlugin) plugin, 89358).getVersion(version -> {
                if (plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                } else {
                    Util.sendMessage(event.getPlayer(),"&aThere is a new update available for SupremeHomes. Download it here: https://www.spigotmc.org/resources/supremehomes.89358/");
                }
            });
        }
    }
}
