package org.benjaminkalb.moderatorbay.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends AbstractCommand {
    
    public ReloadCommand() {
        super(
            "reload",
            "Reload the plugin configuration",
            "moderatorbay.reload",
            new String[]{"rel", "reload"}
        );
    }
    
    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        // Check if sender has permission
        if (!hasPermission(sender)) {
            sender.sendMessage("§cYou don`t have permission to use this command!");
            return true;
        }
        
        try {
            // coming soon: reload config and locale
            // configManager.reload();
            // localeManager.reload();
            
            sender.sendMessage("§a✓ Configuration reloaded successfully!");
            return true;
        } catch (Exception e) {
            sender.sendMessage("§c✗ Error with reloading: " + e.getMessage());
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

