package me.walrus.supremehomes;

import me.walrus.supremehomes.util.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SupremeHomes extends JavaPlugin {

    private static ConfigManager configManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
}
