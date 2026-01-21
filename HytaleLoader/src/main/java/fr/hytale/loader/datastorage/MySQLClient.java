package fr.hytale.loader.datastorage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MySQL client for managing remote MySQL database connections.
 * <p>
 * This class provides a high-level API for interacting with MySQL databases,
 * including connection pooling via HikariCP and common SQL operations.
 * </p>
 * 
 * <h2>Example Usage:</h2>
 * 
 * <pre>
 * // Connect to MySQL
 * MySQLClient mysql = new MySQLClient("localhost", 3306, "minecraft", "root", "password");
 * mysql.connect();
 * 
 * // Create table
 * mysql.execute("CREATE TABLE IF NOT EXISTS players (uuid VARCHAR(36), name VARCHAR(16), coins INT)");
 * 
 * // Insert data
 * mysql.execute("INSERT INTO players VALUES (?, ?, ?)", uuid, name, 1000);
 * 
 * // Query data
 * List&lt;Map&lt;String, Object&gt;&gt; results = mysql.query("SELECT * FROM players WHERE uuid = ?", uuid);
 * 
 * // Close connection
 * mysql.disconnect();
 * </pre>
 * 
 * @author HytaleLoader
 * @version 1.0.7
 * @since 1.0.5
 */
public class MySQLClient {

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private HikariDataSource dataSource;
    private boolean connected;

    /**
     * Creates a new MySQL client.
     * 
     * @param host     The MySQL server hostname or IP
     * @param port     The MySQL server port (default: 3306)
     * @param database The database name
     * @param username The MySQL username
     * @param password The MySQL password
     */
    public MySQLClient(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.connected = false;
    }

    /**
     * Creates a database if it doesn't exist.
     * <p>
     * This is a utility method to create a database before connecting.
     * Call this before creating a MySQLClient instance.
     * </p>
     * 
     * @param host     The MySQL server hostname or IP
     * @param port     The MySQL server port
     * @param database The database name to create
     * @param username The MySQL username
     * @param password The MySQL password
     * @return true if database was created or already exists, false on error
     */
    public static boolean createDatabaseIfNotExists(String host, int port, String database, String username,
            String password) {
        String url = "jdbc:mysql://" + host + ":" + port + "/?allowPublicKeyRetrieval=true&useSSL=false";

        try (Connection conn = DriverManager.getConnection(url, username, password);
                Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + database + "`");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Connects to the MySQL server with connection pooling.
     * 
     * @return true if connected successfully, false otherwise
     */
    public boolean connect() {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
            config.setUsername(username);
            config.setPassword(password);

            // Pool settings
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(10000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);

            // Performance settings
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("useServerPrepStmts", "true");
            config.addDataSourceProperty("useLocalSessionState", "true");
            config.addDataSourceProperty("rewriteBatchedStatements", "true");
            config.addDataSourceProperty("cacheResultSetMetadata", "true");
            config.addDataSourceProperty("cacheServerConfiguration", "true");
            config.addDataSourceProperty("maintainTimeStats", "false");

            dataSource = new HikariDataSource(config);

            // Test connection
            try (Connection conn = dataSource.getConnection()) {
                connected = conn != null && !conn.isClosed();
            }

            return connected;
        } catch (Exception e) {
            e.printStackTrace();
            connected = false;
            return false;
        }
    }

    /**
     * Disconnects from the MySQL server and closes the connection pool.
     */
    public void disconnect() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
        connected = false;
    }

    /**
     * Checks if the client is connected to MySQL.
     * 
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return connected && dataSource != null && !dataSource.isClosed();
    }

    /**
     * Executes an SQL query and returns results.
     * 
     * @param sql    The SQL query
     * @param params Query parameters
     * @return List of rows, where each row is a Map of column-value pairs
     */
    public List<Map<String, Object>> query(String sql, Object... params) {
        List<Map<String, Object>> results = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            setParameters(stmt, params);

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(meta.getColumnName(i), rs.getObject(i));
                    }
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Executes an SQL statement (INSERT, UPDATE, DELETE, CREATE, etc.).
     * 
     * @param sql    The SQL statement
     * @param params Statement parameters
     * @return Number of rows affected, or 0 if error
     */
    public int execute(String sql, Object... params) {
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            setParameters(stmt, params);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Executes a batch of SQL statements.
     * 
     * @param sql        The SQL statement
     * @param paramsList List of parameter arrays
     * @return Array of update counts
     */
    public int[] executeBatch(String sql, List<Object[]> paramsList) {
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Object[] params : paramsList) {
                setParameters(stmt, params);
                stmt.addBatch();
            }

            return stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            return new int[0];
        }
    }

    /**
     * Executes an INSERT and returns the generated key.
     * 
     * @param sql    The INSERT statement
     * @param params Insert parameters
     * @return The generated key, or -1 if error
     */
    public long insert(String sql, Object... params) {
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setParameters(stmt, params);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Executes a query and returns a single result.
     * 
     * @param sql    The SQL query
     * @param params Query parameters
     * @return A map of column-value pairs, or null if no result
     */
    public Map<String, Object> queryOne(String sql, Object... params) {
        List<Map<String, Object>> results = query(sql, params);
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * Executes a query and returns a single value.
     * 
     * @param sql    The SQL query
     * @param params Query parameters
     * @return The value, or null if no result
     */
    public Object queryValue(String sql, Object... params) {
        Map<String, Object> result = queryOne(sql, params);
        if (result != null && !result.isEmpty()) {
            return result.values().iterator().next();
        }
        return null;
    }

    /**
     * Checks if a table exists.
     * 
     * @param tableName The table name
     * @return true if table exists, false otherwise
     */
    public boolean tableExists(String tableName) {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getTables(null, null, tableName, new String[] { "TABLE" })) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Executes multiple SQL statements in a transaction.
     * 
     * @param statements Array of SQL statements with their parameters
     * @return true if transaction succeeded, false otherwise
     */
    public boolean transaction(SQLStatement... statements) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            for (SQLStatement statement : statements) {
                try (PreparedStatement stmt = conn.prepareStatement(statement.sql)) {
                    setParameters(stmt, statement.params);
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Gets direct access to the HikariCP data source.
     * <p>
     * Use this for advanced operations. Remember to close connections properly.
     * </p>
     * 
     * @return The HikariCP data source
     */
    public HikariDataSource getDataSource() {
        return dataSource;
    }

    /**
     * Sets parameters on a prepared statement.
     */
    private void setParameters(PreparedStatement stmt, Object[] params) throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
        }
    }

    /**
     * Helper class for transaction statements.
     */
    public static class SQLStatement {
        final String sql;
        final Object[] params;

        public SQLStatement(String sql, Object... params) {
            this.sql = sql;
            this.params = params;
        }
    }
}
