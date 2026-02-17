package org.benjaminkalb.moderatorbay.managers;

import org.benjaminkalb.moderatorbay.commands.AbstractCommand;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    
    private final JavaPlugin plugin;
    private final Map<String, AbstractCommand> commands;
    
    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.commands = new HashMap<>();
    }
    
     // Initialize and register all commands
    public void init() {
        // registerCommand(new HelpCommand());
        // registerCommand(new ReloadCommand());
    }
    
     // Register a command
    public void registerCommand(AbstractCommand command) {
        commands.put(command.getName().toLowerCase(), command);
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
}
