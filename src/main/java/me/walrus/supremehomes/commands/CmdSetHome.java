package me.walrus.supremehomes.commands;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import me.walrus.supremehomes.wrappers.PlayerData;
import me.walrus.supremehomes.util.Permissions;
import me.walrus.supremehomes.util.Util;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class CmdSetHome {

    @CommandDescription("Set a home")
    @CommandPermission(Permissions.SET_HOME)
    @CommandMethod("sethome|sshome [home]")
    private void setHomeCommand(Player player, @Argument("home") String homeName) {
        Location loc = player.getLocation();
        if (homeName == null) {
            try {
                PlayerData playerData = new PlayerData(player.getUniqueId());
                playerData.addHome("default", loc);
                Util.sendMessage(player, "&aHome '&7default&a' set!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                int length = homeName.length();
                if(length > 24){
                    Util.sendMessage(player, "&cError: The provided Home name is too long. Max length: 24 characters.");
                    return;
                }
                PlayerData playerData = new PlayerData(player.getUniqueId());
                playerData.addHome(homeName, loc);
                Util.sendMessage(player, "&aHome '&7" + homeName + "&a' set!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
