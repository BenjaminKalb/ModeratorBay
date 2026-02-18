package org.benjaminkalb.moderatorbay.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.logging.Level;

public class ConfigManager {
    
    private final JavaPlugin plugin;
    private FileConfiguration config;
    
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }
    
    /**
     * Loads the configuration from config.yml
     */
    public void loadConfig() {
        // Create the data folder if it doesn't exist
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        
        // Copy default config.yml if it doesn't exist
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        
        config = plugin.getConfig();
        plugin.getLogger().log(Level.INFO, "Configuration loaded successfully!");
    }
    
    /**
     * Reloads the configuration
     */
    public void reload() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        plugin.getLogger().log(Level.INFO, "Configuration reloaded successfully!");
    }
    
    /**
     * Gets the database type from config
     */
    public String getDatabaseType() {
        return config.getString("database.type", "SQLite");
    }
    
    /**
     * Gets the SQLite filename
     */
    public String getSQLiteFilename() {
        return config.getString("database.sqlite.filename", "moderatorbay.db");
    }
    
    /**
     * Gets the MySQL host
     */
    public String getMySQLHost() {
        return config.getString("database.mysql.host", "localhost");
    }
    
    /**
     * Gets the MySQL port
     */
    public int getMySQLPort() {
        return config.getInt("database.mysql.port", 3306);
    }
    
    /**
     * Gets the MySQL database name
     */
    public String getMySQLDatabase() {
        return config.getString("database.mysql.database", "moderatorbay");
    }
    
    /**
     * Gets the MySQL username
     */
    public String getMySQLUsername() {
        return config.getString("database.mysql.username", "root");
    }
    
    /**
     * Gets the MySQL password
     */
    public String getMySQLPassword() {
        return config.getString("database.mysql.password", "password");
    }
    
    /**
     * Gets the H2 filename
     */
    public String getH2Filename() {
        return config.getString("database.h2.filename", "moderatorbay");
    }
    
    /**
     * Checks if storage is enabled
     */
    public boolean isStorageEnabled() {
        return config.getBoolean("storage.enabled", true);
    }
    
    /**
     * Gets the auto-save interval in seconds
     */
    public int getAutoSaveInterval() {
        return config.getInt("storage.auto-save-interval", 300);
    }
    
    /**
     * Gets the entire configuration
     */
    public FileConfiguration getConfig() {
        return config;
    }
}
