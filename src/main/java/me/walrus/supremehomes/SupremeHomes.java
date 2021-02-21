package me.walrus.supremehomes;

import com.samjakob.spigui.SpiGUI;
import me.walrus.supremehomes.commands.SupremeManager;
import me.walrus.supremehomes.listeners.JoinListener;
import me.walrus.supremehomes.util.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SupremeHomes extends JavaPlugin {

    private static ConfigManager configManager;
    public static SpiGUI spiGUI;

    @Override
    public void onEnable() {

        spiGUI = new SpiGUI(this);

        // Plugin startup logic
        PluginManager pm = Bukkit.getServer().getPluginManager();
        SupremeManager supremeManager = new SupremeManager(this);
//        Objects.requireNonNull(getCommand("home")).setExecutor(new CmdHome());
//        Objects.requireNonNull(getCommand("listhomes")).setExecutor(new CmdListHomes());
//        Objects.requireNonNull(getCommand("delhome")).setExecutor(new CmdDeleteHome());
//        Objects.requireNonNull(getCommand("sethome")).setExecutor(new CmdSetHome());

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

    public static SpiGUI getGUIManager() {
        return spiGUI;
    }
}
