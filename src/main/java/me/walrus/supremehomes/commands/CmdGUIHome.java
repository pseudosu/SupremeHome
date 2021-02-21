package me.walrus.supremehomes.commands;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.tasks.TaskConsumer;
import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import me.walrus.supremehomes.SupremeHomes;
import me.walrus.supremehomes.network.Home;
import me.walrus.supremehomes.util.Permissions;
import me.walrus.supremehomes.wrappers.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.sql.SQLException;
import java.util.Objects;

public class CmdGUIHome {

    private BukkitCommandManager<CommandSender> manager;

    public CmdGUIHome(BukkitCommandManager<CommandSender> manager) {
        this.manager = manager;
    }

    @CommandDescription("Open  GUI of homes")
    @CommandPermission(Permissions.BASE)
    @CommandMethod("ghome|guihome|gh")
    private void homeCommand(Player player) {
        SGMenu homeMenu = SupremeHomes.getGUIManager().create(player.getName() + "'s homes", 5);
        try {
            PlayerData playerData = new PlayerData(player.getUniqueId());
            for (Home home : playerData.getHomes()) {
                String worldName = home.getWorld();
                if (worldName.equalsIgnoreCase("world")) {
                    homeMenu.addButton(createButton(home, player, Material.GRASS));
                } else if (worldName.equalsIgnoreCase("world_nether")) {
                    System.out.println(worldName);
                    homeMenu.addButton(createButton(home, player, Material.NETHERRACK));
                } else if (worldName.equalsIgnoreCase("world_the_end")) {
                    System.out.println(worldName);

                    homeMenu.addButton(createButton(home, player, Material.ENDER_PEARL));
                }
            }

            this.manager.taskRecipe().begin(homeMenu.getInventory()).synchronous((TaskConsumer<Inventory>) player::openInventory)
                    .execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SGButton createButton(Home home, Player player, Material material) {
        return new SGButton(
                new ItemBuilder(material)
                        .name("&a" + home.getName())
                        .lore("&7x: &a" + home.getX(), "&7y: &a" + home.getY(), "&7z: &a" + home.getZ())
                        .build()
        ).withListener((InventoryClickEvent event) -> {
            if(event.getCurrentItem() == null)
                return;
            String homeName = ChatColor.stripColor(Objects.requireNonNull(event.getCurrentItem()).getItemMeta().getDisplayName());
            Bukkit.getServer().dispatchCommand(player, "home " + homeName);
        });
    }
}
