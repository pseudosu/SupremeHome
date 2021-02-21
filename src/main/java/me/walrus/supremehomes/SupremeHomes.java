package me.walrus.supremehomes;

import com.cloutteam.samjakob.gui.buttons.InventoryListenerGUI;
import me.walrus.supremehomes.commands.CmdDeleteHome;
import me.walrus.supremehomes.commands.CmdHome;
import me.walrus.supremehomes.commands.CmdListHomes;
import me.walrus.supremehomes.commands.CmdSetHome;
import me.walrus.supremehomes.listeners.JoinListener;
import me.walrus.supremehomes.network.DatabaseManager;
import me.walrus.supremehomes.util.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SupremeHomes extends JavaPlugin {

    private static ConfigManager configManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginManager pm = Bukkit.getServer().getPluginManager();
        Objects.requireNonNull(getCommand("home")).setExecutor(new CmdHome());
        Objects.requireNonNull(getCommand("listhomes")).setExecutor(new CmdListHomes());
        Objects.requireNonNull(getCommand("delhome")).setExecutor(new CmdDeleteHome());
        Objects.requireNonNull(getCommand("sethome")).setExecutor(new CmdSetHome());

        pm.registerEvents(new InventoryListenerGUI(), this);
        pm.registerEvents(new JoinListener(), this);

        configManager = new ConfigManager(this);
        configManager.init();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
}
