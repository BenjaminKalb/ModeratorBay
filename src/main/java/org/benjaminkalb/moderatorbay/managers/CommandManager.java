package org.benjaminkalb.moderatorbay.managers;

import org.benjaminkalb.moderatorbay.commands.AbstractCommand;
import org.benjaminkalb.moderatorbay.commands.HelpCommand;
import org.benjaminkalb.moderatorbay.commands.MainCommand;
import org.benjaminkalb.moderatorbay.commands.MuteCommand;
import org.benjaminkalb.moderatorbay.commands.ReloadCommand;
import org.benjaminkalb.moderatorbay.commands.TempmuteCommand;
import org.benjaminkalb.moderatorbay.commands.UnmuteCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class CommandManager {
    
    private final JavaPlugin plugin;
    private final Map<String, AbstractCommand> commands;
    private final Map<String, Map<String, AbstractCommand>> categories;
    private MainCommand mainCommand;
    
    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.commands = new HashMap<>();
        this.categories = new HashMap<>();
    }
    
     // Initialize and register all commands
    public void init() {
        plugin.getLogger().log(Level.INFO, ChatColor.GREEN + "Initializing command manager...");
        
        // Create and register the main command
        mainCommand = new MainCommand();
        mainCommand.registerSubcommand(new HelpCommand());
        mainCommand.registerSubcommand(new ReloadCommand());
        mainCommand.registerSubcommand(new MuteCommand());
        mainCommand.registerSubcommand(new TempmuteCommand(plugin));
        mainCommand.registerSubcommand(new UnmuteCommand());
        
        // Register the main command
        registerCommand(mainCommand);
    }
    
     // Register a command in the default category
    public void registerCommand(AbstractCommand command) {
        registerCommand(command, "general");
    }
    
     // Register a command with a category
    public void registerCommand(AbstractCommand command, String category) {
        // Validate command
        if (command == null) {
            plugin.getLogger().log(Level.WARNING, ChatColor.RED + "Attempted to register null command!");
            return;
        }
        
        // Validate and set default category
        if (category == null || category.isEmpty()) {
            category = "general";
        }
        
        String commandName = command.getName().toLowerCase();
        commands.put(commandName, command);
        
        // Register aliases
        for (String alias : command.getAliases()) {
            commands.put(alias.toLowerCase(), command);
        }
        
        // Register in category
        categories.computeIfAbsent(category, k -> new HashMap<>())
                  .put(commandName, command);
        
        plugin.getLogger().log(Level.INFO, 
            ChatColor.GREEN + "Registered command: '" + command.getName() + 
            "' [Category: " + category + "] with permission: '" + command.getPermission() + "'");
    }
    
     // Execute a command
    public boolean executeCommand(CommandSender sender, Command command, String label, String[] args) {
        AbstractCommand cmd = getCommand(label);
        
        if (cmd == null) {
            sender.sendMessage(ChatColor.RED + "Command not found: /" + label);
            plugin.getLogger().log(Level.WARNING, 
                "Command '" + label + "' executed by " + sender.getName() + " - NOT FOUND");
            return false;
        }
        
        if (!cmd.hasPermission(sender)) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            plugin.getLogger().log(Level.WARNING, 
                "Command '" + label + "' executed by " + sender.getName() + " - PERMISSION DENIED");
            return true;
        }
        
        try {
            plugin.getLogger().log(Level.INFO, 
                ChatColor.GREEN + "Executing command: '" + label + "' by " + sender.getName());
            return cmd.execute(sender, command, label, args);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "An error occurred while executing the command!");
            plugin.getLogger().log(Level.SEVERE, 
                ChatColor.RED + "Error executing command '" + label + "'", e);
            return false;
        }
    }
    
     // Get a command by name
    public AbstractCommand getCommand(String name) {
        return commands.get(name.toLowerCase());
    }
    
     // Check if a command is registered
    public boolean isCommandRegistered(String name) {
        return commands.containsKey(name.toLowerCase());
    }
    
     // Get all registered commands
    public Map<String, AbstractCommand> getCommands() {
        return new HashMap<>(commands);
    }
    
     // Get commands by category
    public Map<String, AbstractCommand> getCommandsByCategory(String category) {
        Map<String, AbstractCommand> result = categories.get(category.toLowerCase());
        return result != null ? new HashMap<>(result) : new HashMap<>();
    }
    
     // Get all categories
    public Map<String, Map<String, AbstractCommand>> getCategories() {
        return new HashMap<>(categories);
    }
    
     // Get category of a command
    public String getCommandCategory(String commandName) {
        String lowerName = commandName.toLowerCase();
        for (Map.Entry<String, Map<String, AbstractCommand>> entry : categories.entrySet()) {
            if (entry.getValue().containsKey(lowerName)) {
                return entry.getKey();
            }
        }
        return "unknown";
    }
    
     // Get total count of commands
    public int getCommandCount() {
        return commands.size();
    }
    
     // Get total count of categories
    public int getCategoryCount() {
        return categories.size();
    }
}
