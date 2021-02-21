package me.walrus.supremehomes.commands;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.tasks.TaskConsumer;
import me.walrus.supremehomes.network.Home;
import me.walrus.supremehomes.wrappers.PlayerData;
import me.walrus.supremehomes.util.Permissions;
import me.walrus.supremehomes.util.Util;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Optional;

public class CmdHome {

    private BukkitCommandManager<CommandSender> manager;

    public CmdHome(BukkitCommandManager<CommandSender> manager){
        this.manager = manager;
    }

    @CommandDescription("Set a home")
    @CommandPermission(Permissions.BASE)
    @CommandMethod("home|shome [home]")
    private void homeCommand(Player player, @Argument("home") String homeName) {
        try {
            PlayerData playerData = new PlayerData(player.getUniqueId());
            if (playerData.getHomes().isEmpty()) {
                Util.sendMessage(player, "&cError: You have not created any homes. Try: &7/sethome&c.");
                return;
            }
            if (homeName == null) {
                Optional<Home> home = playerData.getHomes().stream().filter(o -> o.getName().equalsIgnoreCase("default")).findFirst();
                if (home.isPresent()) {
                    Location location = home.get().getLocation();
                    this.manager.taskRecipe().begin(location).synchronous((TaskConsumer<Location>) player::teleport)
                            .execute(() -> Util.sendMessage(player, "&7Teleporting..."));
                }
            } else {
                Optional<Home> home = playerData.getHomes().stream().filter(o -> o.getName().equalsIgnoreCase(homeName)).findFirst();
                if (home.isPresent()) {
                    Location location = home.get().getLocation();
                    this.manager.taskRecipe().begin(location).synchronous((TaskConsumer<Location>) player::teleport)
                            .execute(() -> Util.sendMessage(player, "&7Teleporting..."));
                } else {
                    Util.sendMessage(player, "&cError: That home does not exist. Try: &7/sethome " + homeName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
