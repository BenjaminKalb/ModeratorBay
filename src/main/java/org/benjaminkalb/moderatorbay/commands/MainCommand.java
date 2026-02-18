package org.benjaminkalb.moderatorbay.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainCommand extends AbstractCommand {
    
    private final Map<String, AbstractCommand> subcommands;
    
    public MainCommand() {
        super("mbay", "ModeratorBay main command", "moderatorbay.use", new String[]{"moderatorbay"});
        this.subcommands = new HashMap<>();
    }
    
    public void registerSubcommand(AbstractCommand subcommand) {
        String commandName = subcommand.getName().toLowerCase();
        subcommands.put(commandName, subcommand);
        
        // Register aliases
        for (String alias : subcommand.getAliases()) {
            subcommands.put(alias.toLowerCase(), subcommand);
        }
    }
    
    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }
        
        String subcommandName = args[0].toLowerCase();
        AbstractCommand subcommand = subcommands.get(subcommandName);
        
        if (subcommand == null) {
            sender.sendMessage(ChatColor.RED + "Unknown subcommand: " + args[0]);
            showHelp(sender);
            return false;
        }
        
        if (!subcommand.hasPermission(sender)) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }
        
        // Prepare arguments for the subcommand (remove the subcommand name from args)
        String[] subcommandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subcommandArgs, 0, args.length - 1);
        
        try {
            return subcommand.execute(sender, command, label, subcommandArgs);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "An error occurred while executing the command!");
            return false;
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            // Suggest subcommands
            for (String subcommandName : subcommands.keySet()) {
                if (subcommandName.startsWith(args[0].toLowerCase())) {
                    AbstractCommand subcmd = subcommands.get(subcommandName);
                    if (subcmd.hasPermission(sender)) {
                        completions.add(subcommandName);
                    }
                }
            }
        } else if (args.length > 1) {
            // Delegate tab completion to the subcommand
            String subcommandName = args[0].toLowerCase();
            AbstractCommand subcommand = subcommands.get(subcommandName);
            
            if (subcommand != null) {
                String[] subcommandArgs = new String[args.length - 1];
                System.arraycopy(args, 1, subcommandArgs, 0, args.length - 1);
                
                List<String> subcompletions = subcommand.onTabComplete(sender, command, alias, subcommandArgs);
                if (subcompletions != null) {
                    completions.addAll(subcompletions);
                }
            }
        }
        
        return completions;
    }
    
    private void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== ModeratorBay Commands ===");
        for (AbstractCommand subcmd : subcommands.values()) {
            if (subcmd.hasPermission(sender)) {
                sender.sendMessage(ChatColor.YELLOW + "/mbay " + subcmd.getName() + ChatColor.GRAY + " - " + subcmd.getDescription());
            }
        }
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPermission() {
        return permission;
    }
    
    public String[] getAliases() {
        return aliases;
    }
}
