package org.benjaminkalb.moderatorbay;

import org.benjaminkalb.moderatorbay.managers.CommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;

public final class ModeratorBay extends JavaPlugin {
    
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "╔════════════════════════════════════╗");
        getLogger().log(Level.INFO, "║   ModeratorBay Plugin Enabled       ║");
        getLogger().log(Level.INFO, "║   Version: " + getDescription().getVersion() + "              ║");
        getLogger().log(Level.INFO, "╚════════════════════════════════════╝");
        
        try {
            commandManager = new CommandManager(this);
            commandManager.init();
            getLogger().log(Level.INFO, "✓ Command manager initialized successfully");
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "✗ Failed to initialize command manager", e);
        }
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "╔════════════════════════════════════╗");
        getLogger().log(Level.INFO, "║   ModeratorBay Plugin Disabled      ║");
        getLogger().log(Level.INFO, "╚════════════════════════════════════╝");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return commandManager.executeCommand(sender, command, label, args);
    }
}
