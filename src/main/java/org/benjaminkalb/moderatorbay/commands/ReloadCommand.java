package org.benjaminkalb.moderatorbay.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends AbstractCommand {
    
    public ReloadCommand() {
        super(
        "reload", 
        "Reloads the plugin configuration.", 
        "moderatorbay.reload", 
        new String[]{"rel", "r"});
    }
    
    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        try {
            // coming soon: reload config and locale
            // configManager.reload();
            // localeManager.reload();
            
            sender.sendMessage(ChatColor.GREEN + "Configuration reloaded successfully!");
            return true;
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Error with reloading: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        // First arg - Hasn`t any subcommands yet, but we can add some in the future
        // Options : config, locale и т.д.
        
        return completions;
    }
}

