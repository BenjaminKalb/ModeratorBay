package org.benjaminkalb.moderatorbay.commands;

import org.bukkit.ChatColor;
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
        sender.sendMessage(ChatColor.GREEN + "Available commands:");
        sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + "/mbay help: " + ChatColor.GRAY + "Displays this help message.");
        sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + "/mbay reload: " + ChatColor.GRAY + "Reloads the plugin configuration.");
        sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + "/mbay mute <player>: " + ChatColor.GRAY + "Permanently mutes a player.");
        sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + "/mbay tempmute <player> <seconds>: " + ChatColor.GRAY + "Temporarily mutes a player.");
        sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + "/mbay ban <player>: " + ChatColor.GRAY + "Bans a player from the server.");
        sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + "/mbay unban <player>: " + ChatColor.GRAY + "Unbans a player from the server.");
        sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + "/mbay kick <player>: " + ChatColor.GRAY + "Kicks a player from the server.");
        sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + "/mbay warn <player> <reason>: " + ChatColor.GRAY + "Warns a player with a specified reason.");
        sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + "/mbay unmute <player>: " + ChatColor.GRAY + "Unmutes a player.");
        sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + "/mbay tempban <player> <duration>: " + ChatColor.GRAY + "Temporarily bans a player for a specified duration.");
        sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + "/mbay history <player>: " + ChatColor.GRAY + "Displays the moderation history of a player.");
        sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + "/mbay purge <player>: " + ChatColor.GRAY + "Purges all moderation actions for a player.");
        sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + "/mbay info <player>: " + ChatColor.GRAY + "Displays information about a player.");
        sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + "/mbay list: " + ChatColor.GRAY + "Lists all currently moderated players.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // No tab completion for the help command
        return Collections.emptyList();
    }
    
}
