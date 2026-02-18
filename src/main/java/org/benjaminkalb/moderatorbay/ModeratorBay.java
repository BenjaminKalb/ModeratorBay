package org.benjaminkalb.moderatorbay;

import org.benjaminkalb.moderatorbay.managers.CommandManager;
import org.benjaminkalb.moderatorbay.managers.ConfigManager;
import org.benjaminkalb.moderatorbay.utils.DatabaseUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;

public final class ModeratorBay extends JavaPlugin {
    
    private CommandManager commandManager;
    private ConfigManager configManager;
    private DatabaseUtil databaseUtil;

    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, ChatColor.GREEN + "ModeratorBay Plugin Enabled");
        getLogger().log(Level.INFO, ChatColor.GRAY + "Current Version: " + getDescription().getVersion() + " by " + getDescription().getAuthors());
        
        try {
            // Initialize ConfigManager
            configManager = new ConfigManager(this);
            getLogger().log(Level.INFO, ChatColor.GREEN + "Config manager initialized");
            
            // Initialize DatabaseUtil
            if (configManager.isStorageEnabled()) {
                String dbType = configManager.getDatabaseType();
                databaseUtil = new DatabaseUtil(
                    this,
                    dbType,
                    configManager.getSQLiteFilename(),
                    configManager.getMySQLHost(),
                    configManager.getMySQLPort(),
                    configManager.getMySQLDatabase(),
                    configManager.getMySQLUsername(),
                    configManager.getMySQLPassword()
                );
                
                if (databaseUtil.connect()) {
                    databaseUtil.initializeTables();
                    getLogger().log(Level.INFO, ChatColor.GREEN + "Database initialized successfully");
                } else {
                    getLogger().log(Level.SEVERE, ChatColor.RED + "Failed to connect to database!");
                }
            }
            
            // Initialize CommandManager
            commandManager = new CommandManager(this);
            commandManager.init();
            getLogger().log(Level.INFO, ChatColor.GREEN + "Command manager initialized successfully");
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, ChatColor.RED + "Failed to initialize plugin", e);
        }
    }

    @Override
    public void onDisable() {
        if (databaseUtil != null) {
            databaseUtil.disconnect();
        }
        getLogger().log(Level.INFO, ChatColor.GREEN + "ModeratorBay Plugin Disabled");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return commandManager.executeCommand(sender, command, label, args);
    }
    
    public DatabaseUtil getDatabaseUtil() {
        return databaseUtil;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public CommandManager getCommandManager() {
        return commandManager;
    }
}
