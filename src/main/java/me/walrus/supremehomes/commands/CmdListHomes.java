package me.walrus.supremehomes.commands;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import me.walrus.supremehomes.wrappers.PlayerData;
import me.walrus.supremehomes.util.Permissions;
import me.walrus.supremehomes.util.Util;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class CmdListHomes{

    @CommandDescription("List homes")
    @CommandPermission(Permissions.BASE)
    @CommandMethod("listhomes|listhome|lhome")
    private void listHomesCommand(Player player) {
        try {
            PlayerData playerData = new PlayerData(player.getUniqueId());
            if (playerData.getHomes().isEmpty()) {
                Util.sendMessage(player, "&cError: You have not created any homes. Try: &7/sethome&c.");
            }else{
                Util.sendMessage(player, "&aHomes: &7" + playerData.getHomesAsString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
