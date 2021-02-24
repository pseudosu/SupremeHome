package me.walrus.supremehomes;

import com.samjakob.spigui.SpiGUI;
import me.walrus.supremehomes.commands.SupremeManager;
import me.walrus.supremehomes.listeners.JoinListener;
import me.walrus.supremehomes.network.DatabaseManager;
import me.walrus.supremehomes.network.UpdateChecker;
import me.walrus.supremehomes.util.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.logging.Logger;

public final class SupremeHomes extends JavaPlugin {

    private static ConfigManager configManager;
    private static SpiGUI spiGUI;

    @Override
    public void onEnable() {

        spiGUI = new SpiGUI(this);

        // Plugin startup logic
        PluginManager pm = Bukkit.getServer().getPluginManager();
        SupremeManager supremeManager = new SupremeManager(this);
        pm.registerEvents(new JoinListener(this), this);

        configManager = new ConfigManager(this);
        configManager.init();

        try {
            DatabaseManager.initDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Logger logger = this.getLogger();

        new UpdateChecker(this, 89358).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logger.info("SupremeHomes is up-to-date.");
            } else {
                logger.info("There is a new update available. Download it here: https://www.spigotmc.org/resources/supremehomes.89358/");
            }
        });

    }
    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static SpiGUI getGUIManager() {
        return spiGUI;
    }
}
