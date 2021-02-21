package me.walrus.supremehomes.commands;

import me.walrus.supremehomes.network.Home;
import me.walrus.supremehomes.network.PlayerData;
import me.walrus.supremehomes.util.Permissions;
import me.walrus.supremehomes.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Optional;

public class CmdHome implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be ran as a Player.");
            return false;
        }
        Player player = (Player) sender;
        if (!player.hasPermission(Permissions.BASE)) {
            Util.sendMessage(player, "&cError: You do not have permission to perform this command.");
            return false;
        }
        try {
            PlayerData playerData = new PlayerData(player.getUniqueId());
            if (playerData.getHomes().isEmpty()) {
                Util.sendMessage(player, "&cError: You have not created any homes. Try: &7/sethome&c.");
                return false;
            }
            if (args.length == 0) {
                Optional<Home> home = playerData.getHomes().stream().filter(o -> o.getName().equalsIgnoreCase("default")).findFirst();
                if (home.isPresent()) {
                    player.teleport(home.get().getLocation());
                    Util.sendMessage(player, "&7Teleporting...");
                    return true;
                }
            }else{
                String toHomeName = args[0];
                Optional<Home> home = playerData.getHomes().stream().filter(o -> o.getName().equalsIgnoreCase(toHomeName)).findFirst();
                if (home.isPresent()) {
                    player.teleport(home.get().getLocation());
                    Util.sendMessage(player, "&7Teleporting...");
                    return true;
                }else{
                    Util.sendMessage(player, "&cError: That home does not exist. Try: &7/sethome " + args[0]);
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
