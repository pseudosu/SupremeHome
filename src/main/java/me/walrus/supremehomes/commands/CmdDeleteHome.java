package me.walrus.supremehomes.commands;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import cloud.commandframework.bukkit.BukkitCommandManager;
import me.walrus.supremehomes.network.PlayerData;
import me.walrus.supremehomes.util.Permissions;
import me.walrus.supremehomes.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class CmdDeleteHome {
    @CommandDescription("Delete a home")
    @CommandPermission(Permissions.DELETE_HOME)
    @CommandMethod("delhome|dhome <home>")
    private void deleteCommand(Player player, @Argument("home") String homeName) {
        try {
            PlayerData playerData = new PlayerData(player.getUniqueId());
            if(playerData.deleteHome(homeName)){
                Util.sendMessage(player, "&aHome deleted.");
            }else{
                Util.sendMessage(player, "&cError: Could not delete home. Home does not exist. Try /listhomes");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
