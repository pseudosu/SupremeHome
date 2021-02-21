package me.walrus.supremehomes.util;

import org.bukkit.plugin.Plugin;

import java.util.List;

public class ConfigManager {

    Plugin p;
    public final String CONFIG_PATH = "configuration.settings.";


    public ConfigManager(Plugin p) {
        this.p = p;
    }

    public void init() {
        p.getConfig().options().copyDefaults(true);
        p.getConfig().addDefault(CONFIG_PATH + "mysql.host", "localhost");
        p.getConfig().addDefault(CONFIG_PATH + "mysql.port", "3306");
        p.getConfig().addDefault(CONFIG_PATH + "mysql.database", "supremehomes");
        p.getConfig().addDefault(CONFIG_PATH + "mysql.username", "supremehomes");
        p.getConfig().addDefault(CONFIG_PATH + "mysql.password", "SecurePassword");

        p.saveConfig();
    }

    /**
     * @param path string path to use
     * @return string
     */
    public String getString(String path) {
        return p.getConfig().getString(CONFIG_PATH + path);
    }

    /**
     * @param path string path to use
     * @return string array
     */
    public List<String> getStringArray(String path) {
        return p.getConfig().getStringList(CONFIG_PATH + path);
    }

    /**
     * @return mysql host
     */
    public String getMYSQL_HOST() {
        return getString("mysql.host");
    }

    /**
     * @return mysql database
     */
    public String getMYSQL_DATABASE() {
        return getString("mysql.database");
    }

    /**
     * @return mysql username
     */
    public String getMYSQL_USERNAME() {
        return getString("mysql.username");
    }

    /**
     * @return mysql password
     */
    public String getMYSQL_PASSWORD() {
        return getString("mysql.password");
    }
    public String getMYSQL_PORT(){return getString("mysl.port");}
}
