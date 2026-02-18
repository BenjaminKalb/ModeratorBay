package org.benjaminkalb.moderatorbay.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
// import org.bukkit.command.TabCompleter;
// import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class HelpCommand extends AbstractCommand {

    public HelpCommand() {
        super("help", "Displays a list of available commands.", 
        "moderatorbay.help", 
        new String[]{"h", "commands"});
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        // Implementation to display help information about available commands
        // This list can be dynamically generated based on registered commands, but for simplicity we're just outputting a static list.
        // These are sample commands that may be implemented in the future.
        // For now, this is a test output that can be replaced with a dynamic one in the future.
        sender.sendMessage("Available commands:");
        sender.sendMessage(" - /moderatorbay help: Displays this help message.");
        sender.sendMessage(" - /moderatorbay reload: Reloads the plugin configuration.");
        sender.sendMessage(" - /moderatorbay ban <player>: Bans a player from the server.");
        sender.sendMessage(" - /moderatorbay unban <player>: Unbans a player from the server.");
        sender.sendMessage(" - /moderatorbay kick <player>: Kicks a player from the server.");
        sender.sendMessage(" - /moderatorbay warn <player> <reason>: Warns a player with a specified reason.");
        sender.sendMessage(" - /moderatorbay mute <player> <duration>: Mutes a player for a specified duration.");
        sender.sendMessage(" - /moderatorbay unmute <player>: Unmutes a player.");
        sender.sendMessage(" - /moderatorbay tempban <player> <duration>: Temporarily bans a player for a specified duration.");
        sender.sendMessage(" - /moderatorbay tempmute <player> <duration>: Temporarily mutes a player for a specified duration.");
        sender.sendMessage(" - /moderatorbay history <player>: Displays the moderation history of a player.");
        sender.sendMessage(" - /moderatorbay purge <player>: Purges all moderation actions for a player.");
        sender.sendMessage(" - /moderatorbay info <player>: Displays information about a player.");
        sender.sendMessage(" - /moderatorbay list: Lists all currently moderated players.");
        // Here you would typically loop through your registered commands and display their descriptions
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // No tab completion for the help command
        return Collections.emptyList();
    }
    
}
