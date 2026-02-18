package org.benjaminkalb.moderatorbay.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TempmuteCommand extends AbstractCommand {
    
    private static final Map<String, Long> tempMutedPlayers = new HashMap<>();
    private static JavaPlugin plugin;
    
    public TempmuteCommand(JavaPlugin plugin) {
        super(
            "tempmute",
            "Temporarily mutes a player for a specified duration",
            "moderatorbay.tempmute",
            new String[]{"tm", "tmute"}
        );
        TempmuteCommand.plugin = plugin;
    }
    
    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /mbay tempmute <player> <duration>");
            sender.sendMessage(ChatColor.GRAY + "Example: /mbay tempmute PlayerName 60 (60 seconds)");
            return false;
        }
        
        String playerName = args[0];
        String durationStr = args[1];
        long duration;
        
        try {
            duration = Long.parseLong(durationStr) * 20L; // Convert seconds to ticks (1 tick = 50ms)
            if (duration <= 0) {
                sender.sendMessage(ChatColor.RED + "Duration must be greater than 0!");
                return false;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid duration: " + durationStr);
            return false;
        }
        
        Player targetPlayer = Bukkit.getPlayer(playerName);
        
        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.RED + "Player not found: " + playerName);
            return false;
        }
        
        if (tempMutedPlayers.containsKey(targetPlayer.getName())) {
            sender.sendMessage(ChatColor.YELLOW + targetPlayer.getName() + " is already temporarily muted!");
            return true;
        }
        
        long unmuteTime = System.currentTimeMillis() + (duration * 50);
        tempMutedPlayers.put(targetPlayer.getName(), unmuteTime);
        
        sender.sendMessage(ChatColor.GREEN + targetPlayer.getName() + " has been muted for " + durationStr + " seconds.");
        targetPlayer.sendMessage(ChatColor.RED + "You have been muted by " + sender.getName() + " for " + durationStr + " seconds.");
        
        // Broadcast to ops
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp() && !player.equals(sender)) {
                player.sendMessage(ChatColor.GRAY + sender.getName() + " temporarily muted " + targetPlayer.getName() + " for " + durationStr + " seconds.");
            }
        }
        
        // Schedule unmute
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, () -> {
            tempMutedPlayers.remove(targetPlayer.getName());
            if (targetPlayer.isOnline()) {
                targetPlayer.sendMessage(ChatColor.GREEN + "Your mute has expired!");
            }
        }, duration);
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            String input = args[0].toLowerCase();
            
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(input)) {
                    completions.add(player.getName());
                }
            }
            
            return completions;
        } else if (args.length == 2) {
            // Suggest duration examples
            List<String> completions = new ArrayList<>();
            String input = args[1];
            
            String[] suggestions = {"10", "30", "60", "300", "600", "1800"};
            for (String suggestion : suggestions) {
                if (suggestion.startsWith(input)) {
                    completions.add(suggestion);
                }
            }
            
            return completions;
        }
        
        return Collections.emptyList();
    }
    
    public static boolean isTempMuted(Player player) {
        String playerName = player.getName();
        if (!tempMutedPlayers.containsKey(playerName)) {
            return false;
        }
        
        long unmuteTime = tempMutedPlayers.get(playerName);
        if (System.currentTimeMillis() >= unmuteTime) {
            tempMutedPlayers.remove(playerName);
            return false;
        }
        
        return true;
    }
    
    public static void removeTempMute(String playerName) {
        tempMutedPlayers.remove(playerName);
    }
}
