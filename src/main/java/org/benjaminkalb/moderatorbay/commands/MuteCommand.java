package org.benjaminkalb.moderatorbay.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MuteCommand extends AbstractCommand {
    
    private static final Set<String> mutedPlayers = new HashSet<>();
    
    public MuteCommand() {
        super(
            "mute",
            "Permanently mutes a player",
            "moderatorbay.mute",
            new String[]{"m"}
        );
    }
    
    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /mbay mute <player>");
            return false;
        }
        
        String playerName = args[0];
        Player targetPlayer = Bukkit.getPlayer(playerName);
        
        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.RED + "Player not found: " + playerName);
            return false;
        }
        
        if (mutedPlayers.contains(targetPlayer.getName())) {
            sender.sendMessage(ChatColor.YELLOW + targetPlayer.getName() + " is already muted!");
            return true;
        }
        
        mutedPlayers.add(targetPlayer.getName());
        sender.sendMessage(ChatColor.GREEN + targetPlayer.getName() + " has been muted.");
        targetPlayer.sendMessage(ChatColor.RED + "You have been muted by " + sender.getName());
        
        // Broadcast to ops
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp() && !player.equals(sender)) {
                player.sendMessage(ChatColor.GRAY + sender.getName() + " muted " + targetPlayer.getName());
            }
        }
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            String input = args[0].toLowerCase();
            
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(input) && !mutedPlayers.contains(player.getName())) {
                    completions.add(player.getName());
                }
            }
            
            return completions;
        }
        
        return Collections.emptyList();
    }
    
    public static boolean isMuted(Player player) {
        return mutedPlayers.contains(player.getName());
    }
    
    public static void unmute(String playerName) {
        mutedPlayers.remove(playerName);
    }
}
