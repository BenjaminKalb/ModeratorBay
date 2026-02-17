package org.benjaminkalb.moderatorbay.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand implements TabCompleter {
    
    protected String name;
    protected String description;
    protected String permission;
    protected String[] aliases;
    
    public AbstractCommand(String name, String description, String permission, String[] aliases) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.aliases = aliases;
    }
    
     // Main method to execute the command
    public abstract boolean execute(CommandSender sender, Command command, String label, String[] args);
    
     // Auto-completion for the command
    @Override
    public abstract List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args);
    
     // Check if sender has permission to execute the command
    public boolean hasPermission(CommandSender sender) {
        if (permission == null || permission.isEmpty()) {
            return true;
        }
        return sender.hasPermission(permission);
    }

     // Check if the sender is a player
    public boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

     // Get the player from the sender if it is a player, otherwise return null
    public Player getPlayer(CommandSender sender) {
        if (isPlayer(sender)) {
            return (Player) sender;
        }
        return null;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getPermission() {
        return permission;
    }
    
    public String[] getAliases() {
        return aliases;
    }
}
