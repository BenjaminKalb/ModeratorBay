package org.benjaminkalb.moderatorbay.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnmuteCommand extends AbstractCommand {
    
    public UnmuteCommand() {
        super(
            "unmute",
            "Unmutes a player",
            "moderatorbay.unmute",
            new String[]{"um"}
        );
    }
    
    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /mbay unmute <player>");
            return false;
        }
        
        String playerName = args[0];
        Player targetPlayer = Bukkit.getPlayer(playerName);
        
        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.RED + "Player not found: " + playerName);
            return false;
        }
        
        boolean wasMuted = false;
        
        // Check if muted by MuteCommand
        if (MuteCommand.isMuted(targetPlayer)) {
            MuteCommand.unmute(targetPlayer.getName());
            wasMuted = true;
        }
        
        // Check if muted by TempmuteCommand
        if (TempmuteCommand.isTempMuted(targetPlayer)) {
            TempmuteCommand.removeTempMute(targetPlayer.getName());
            wasMuted = true;
        }
        
        if (!wasMuted) {
            sender.sendMessage(ChatColor.YELLOW + targetPlayer.getName() + " is not muted!");
            return true;
        }
        
        sender.sendMessage(ChatColor.GREEN + targetPlayer.getName() + " has been unmuted.");
        targetPlayer.sendMessage(ChatColor.GREEN + "You have been unmuted by " + sender.getName());
        
        // Broadcast to ops
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp() && !player.equals(sender)) {
                player.sendMessage(ChatColor.GRAY + sender.getName() + " unmuted " + targetPlayer.getName());
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
                if (player.getName().toLowerCase().startsWith(input) && 
                    (MuteCommand.isMuted(player) || TempmuteCommand.isTempMuted(player))) {
                    completions.add(player.getName());
                }
            }
            
            return completions;
        }
        
        return Collections.emptyList();
    }
}
