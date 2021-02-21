package me.walrus.supremehomes.commands;

import me.walrus.supremehomes.network.PlayerData;
import me.walrus.supremehomes.util.Permissions;
import me.walrus.supremehomes.util.Util;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class CmdSetHome implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be ran as a Player.");
            return false;
        }
        Player player = (Player) sender;
        if (!player.hasPermission(Permissions.BASE) || !player.hasPermission(Permissions.SET_HOME)) {
            Util.sendMessage(player, "&cError: You do not have permission to perform this command.");
            return false;
        }
        Location loc = player.getLocation();

        if (args.length == 0) {
            try {
                PlayerData playerData = new PlayerData(player.getUniqueId());
                playerData.addHome("default", loc);
                Util.sendMessage(player, "&aHome '&7default&a' set!");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                PlayerData playerData = new PlayerData(player.getUniqueId());
                playerData.addHome(args[0], loc);
                Util.sendMessage(player, "&aHome '&7" + args[0] + "&a' set!");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
