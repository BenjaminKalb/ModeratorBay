package org.benjaminkalb.moderatorbay.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PunishmentRepository {
    
    private final DatabaseUtil database;
    
    public PunishmentRepository(DatabaseUtil database) {
        this.database = database;
    }
    

    public boolean addMute(String playerName, String muteType, long muteTime, 
                          String reason, String moderatorName) {
        String sql = "INSERT INTO mutes (player_name, mute_type, mute_time, reason, moderator_name) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, playerName);
            ps.setString(2, muteType);
            ps.setLong(3, muteTime);
            ps.setString(4, reason);
            ps.setString(5, moderatorName);
            
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean removeMute(String playerName) {
        String sql = "UPDATE mutes SET is_active = FALSE WHERE player_name = ? AND is_active = TRUE";
        
        try (Connection conn = database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, playerName);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<String> getActiveMutes(String playerName) {
        List<String> mutes = new ArrayList<>();
        String sql = "SELECT * FROM mutes WHERE player_name = ? AND is_active = TRUE";
        
        try (Connection conn = database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                mutes.add(rs.getString("mute_type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return mutes;
    }
    
    public boolean addBan(String playerName, String banType, long banTime, 
                         String reason, String moderatorName) {
        String sql = "INSERT INTO bans (player_name, ban_type, ban_time, reason, moderator_name) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, playerName);
            ps.setString(2, banType);
            ps.setLong(3, banTime);
            ps.setString(4, reason);
            ps.setString(5, moderatorName);
            
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean removeBan(String playerName) {
        String sql = "UPDATE bans SET is_active = FALSE WHERE player_name = ? AND is_active = TRUE";
        
        try (Connection conn = database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, playerName);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Gets ban history for a player
     */
    public List<String> getBanHistory(String playerName) {
        List<String> bans = new ArrayList<>();
        String sql = "SELECT * FROM bans WHERE player_name = ? ORDER BY created_at DESC";
        
        try (Connection conn = database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                String banInfo = "Ban: " + rs.getString("ban_type") + " - Reason: " + rs.getString("reason");
                bans.add(banInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return bans;
    }
    

    public boolean addWarn(String playerName, String reason, String moderatorName) {
        String sql = "INSERT INTO warns (player_name, reason, moderator_name, warn_time) " +
                    "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, playerName);
            ps.setString(2, reason);
            ps.setString(3, moderatorName);
            ps.setLong(4, System.currentTimeMillis());
            
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getWarnCount(String playerName) {
        String sql = "SELECT COUNT(*) as count FROM warns WHERE player_name = ?";
        
        try (Connection conn = database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    public boolean addKick(String playerName, String reason, String moderatorName) {
        String sql = "INSERT INTO kicks (player_name, reason, moderator_name, kick_time) " +
                    "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, playerName);
            ps.setString(2, reason);
            ps.setString(3, moderatorName);
            ps.setLong(4, System.currentTimeMillis());
            
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<String> getPlayerHistory(String playerName) {
        List<String> history = new ArrayList<>();
        String sql = "SELECT * FROM punishments WHERE player_name = ? ORDER BY created_at DESC";
        
        try (Connection conn = database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                String entry = rs.getString("punishment_type") + ": " + rs.getString("reason") + 
                              " (by " + rs.getString("moderator_name") + ")";
                history.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return history;
    }
    
    public boolean addPunishment(String playerName, String punishmentType, 
                                String reason, String moderatorName, long duration) {
        String sql = "INSERT INTO punishments (player_name, punishment_type, reason, moderator_name, punishment_time, duration) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, playerName);
            ps.setString(2, punishmentType);
            ps.setString(3, reason);
            ps.setString(4, moderatorName);
            ps.setLong(5, System.currentTimeMillis());
            ps.setLong(6, duration);
            
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
