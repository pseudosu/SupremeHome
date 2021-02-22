package me.walrus.supremehomes.listeners;

import me.walrus.supremehomes.network.DatabaseManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
//        try {
//            DatabaseManager.initializePlayer(event.getPlayer().getUniqueId().toString());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}
