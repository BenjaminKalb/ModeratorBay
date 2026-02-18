package org.benjaminkalb.moderatorbay.utils;

import org.bukkit.plugin.java.JavaPlugin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.logging.Level;

public class DatabaseUtil {
    
    private final JavaPlugin plugin;
    private Connection connection;
    private final String dbType;
    private final String dbPath;
    private final String dbHost;
    private final int dbPort;
    private final String dbName;
    private final String dbUser;
    private final String dbPassword;
    
    public DatabaseUtil(JavaPlugin plugin, String dbType, String dbPath, 
                       String dbHost, int dbPort, String dbName, 
                       String dbUser, String dbPassword) {
        this.plugin = plugin;
        this.dbType = dbType;
        this.dbPath = dbPath;
        this.dbHost = dbHost;
        this.dbPort = dbPort;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }
    
    public boolean connect() {
        try {
            if (dbType.equalsIgnoreCase("SQLite")) {
                Class.forName("org.sqlite.JDBC");
                String url = "jdbc:sqlite:" + plugin.getDataFolder() + "/" + dbPath;
                connection = DriverManager.getConnection(url);
            } else if (dbType.equalsIgnoreCase("MySQL")) {
                Class.forName("com.mysql.jdbc.Driver");
                String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
                connection = DriverManager.getConnection(url, dbUser, dbPassword);
            } else if (dbType.equalsIgnoreCase("H2")) {
                Class.forName("org.h2.Driver");
                String url = "jdbc:h2:" + plugin.getDataFolder() + "/" + dbPath;
                connection = DriverManager.getConnection(url);
            } else {
                plugin.getLogger().log(Level.SEVERE, "Unknown database type: " + dbType);
                return false;
            }
            
            plugin.getLogger().log(Level.INFO, "Connected to " + dbType + " database successfully!");
            return true;
        } catch (ClassNotFoundException e) {
            plugin.getLogger().log(Level.SEVERE, "Database driver not found: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to connect to database: " + e.getMessage());
            return false;
        }
    }
    
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                plugin.getLogger().log(Level.INFO, "Disconnected from database");
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Error disconnecting from database: " + e.getMessage());
            }
        }
    }
    
    public boolean initializeTables() {
        try {
            createMutesTable();
            createBansTable();
            createWarnsTable();
            createKicksTable();
            createPunishmentsTable();
            
            try {
                executeUpdate("CREATE INDEX IF NOT EXISTS idx_player ON punishments(player_name)");
                executeUpdate("CREATE INDEX IF NOT EXISTS idx_type ON punishments(punishment_type)");
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Could not create indexes: " + e.getMessage());
            }
            
            plugin.getLogger().log(Level.INFO, "All database tables created successfully!");
            return true;
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create tables: " + e.getMessage());
            return false;
        }
    }
    
    private void createMutesTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS mutes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "player_name VARCHAR(50) NOT NULL," +
                "player_uuid VARCHAR(36)," +
                "mute_type VARCHAR(20) NOT NULL," +
                "mute_time BIGINT NOT NULL," +
                "unmute_time BIGINT," +
                "reason VARCHAR(255)," +
                "moderator_name VARCHAR(50)," +
                "is_active BOOLEAN DEFAULT TRUE," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "UNIQUE(player_name, mute_time)" +
                ")";
        
        executeUpdate(sql);
    }
    
    private void createBansTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS bans (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "player_name VARCHAR(50) NOT NULL," +
                "player_uuid VARCHAR(36)," +
                "ban_type VARCHAR(20) NOT NULL," +
                "ban_time BIGINT NOT NULL," +
                "unban_time BIGINT," +
                "reason VARCHAR(255)," +
                "moderator_name VARCHAR(50)," +
                "is_active BOOLEAN DEFAULT TRUE," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "UNIQUE(player_name, ban_time)" +
                ")";
        
        executeUpdate(sql);
    }
    
    private void createWarnsTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS warns (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "player_name VARCHAR(50) NOT NULL," +
                "player_uuid VARCHAR(36)," +
                "warn_count INTEGER DEFAULT 1," +
                "reason VARCHAR(255)," +
                "moderator_name VARCHAR(50)," +
                "warn_time BIGINT NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "UNIQUE(player_name, warn_time)" +
                ")";
        
        executeUpdate(sql);
    }
    
    private void createKicksTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS kicks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "player_name VARCHAR(50) NOT NULL," +
                "player_uuid VARCHAR(36)," +
                "reason VARCHAR(255)," +
                "moderator_name VARCHAR(50)," +
                "kick_time BIGINT NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        
        executeUpdate(sql);
    }
    
    private void createPunishmentsTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS punishments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "player_name VARCHAR(50) NOT NULL," +
                "player_uuid VARCHAR(36)," +
                "punishment_type VARCHAR(20) NOT NULL," +
                "reason VARCHAR(255)," +
                "moderator_name VARCHAR(50)," +
                "punishment_time BIGINT NOT NULL," +
                "duration BIGINT," +
                "is_active BOOLEAN DEFAULT TRUE," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        
        executeUpdate(sql);
    }
    
    public int executeUpdate(String sql) throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Database connection is not established");
        }
        
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(sql);
        }
    }
    
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public String getDatabaseType() {
        return dbType;
    }
}
