package me.walrus.supremehomes.commands;

import me.walrus.supremehomes.network.PlayerData;
import me.walrus.supremehomes.util.Permissions;
import me.walrus.supremehomes.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class CmdDeleteHome implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be ran as a Player.");
            return false;
        }
        Player player = (Player) sender;
        if (!player.hasPermission(Permissions.BASE) || !player.hasPermission(Permissions.DELETE_HOME)) {
            Util.sendMessage(player, "&cError: You do not have permission to perform this command.");
            return false;
        }
        if(args.length == 0){
            Util.sendMessage(player, "&cError: Invalid syntax. Usage: &7/delhome [name]");
            return false;
        }

        try {
            PlayerData playerData = new PlayerData(player.getUniqueId());
            if(playerData.deleteHome(args[0])){
                Util.sendMessage(player, "&aHome deleted.");
                return true;
            }else{
                Util.sendMessage(player, "&cError: Could not delete home. Home does not exist. Try /listhomes");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
