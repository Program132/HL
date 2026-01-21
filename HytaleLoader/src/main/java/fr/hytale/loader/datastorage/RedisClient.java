package fr.hytale.loader.datastorage;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis client for managing remote Redis database connections.
 * <p>
 * This class provides a high-level API for interacting with Redis,
 * including connection pooling, automatic reconnection, and common operations.
 * </p>
 * 
 * <h2>Example Usage:</h2>
 * 
 * <pre>
 * // Connect to Redis
 * RedisClient redis = new RedisClient("127.0.0.1", 6379);
 * redis.connect();
 * 
 * // Store and retrieve data
 * redis.set("player:123:coins", "1000");
 * String coins = redis.get("player:123:coins");
 * 
 * // Hash operations
 * redis.hset("player:123", "name", "Steve");
 * redis.hset("player:123", "level", "42");
 * Map&lt;String, String&gt; data = redis.hgetAll("player:123");
 * 
 * // Close connection
 * redis.disconnect();
 * </pre>
 * 
 * @author HytaleLoader
 * @version 1.0.7
 * @since 1.0.5
 */
public class RedisClient {

    private final String host;
    private final int port;
    private final String password;
    private final int database;
    private JedisPool pool;
    private boolean connected;

    /**
     * Creates a new Redis client.
     * 
     * @param host The Redis server hostname or IP address
     * @param port The Redis server port (default: 6379)
     */
    public RedisClient(String host, int port) {
        this(host, port, null, 0);
    }

    /**
     * Creates a new Redis client with authentication.
     * 
     * @param host     The Redis server hostname or IP address
     * @param port     The Redis server port
     * @param password The Redis password (null if no auth)
     */
    public RedisClient(String host, int port, String password) {
        this(host, port, password, 0);
    }

    /**
     * Creates a new Redis client with full configuration.
     * 
     * @param host     The Redis server hostname or IP address
     * @param port     The Redis server port
     * @param password The Redis password (null if no auth)
     * @param database The database index (0-15)
     */
    public RedisClient(String host, int port, String password, int database) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.database = database;
        this.connected = false;
    }

    /**
     * Connects to the Redis server with connection pooling.
     * 
     * @return true if connected successfully, false otherwise
     */
    public boolean connect() {
        try {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(128);
            poolConfig.setMaxIdle(64);
            poolConfig.setMinIdle(16);
            poolConfig.setTestOnBorrow(true);
            poolConfig.setTestOnReturn(true);
            poolConfig.setTestWhileIdle(true);
            poolConfig.setMinEvictableIdleTime(Duration.ofSeconds(60));
            poolConfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(30));

            if (password != null && !password.isEmpty()) {
                pool = new JedisPool(poolConfig, host, port, 2000, password, database);
            } else {
                pool = new JedisPool(poolConfig, host, port, 2000);
            }

            // Test connection
            try (Jedis jedis = pool.getResource()) {
                jedis.ping();
            }

            connected = true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            connected = false;
            return false;
        }
    }

    /**
     * Disconnects from the Redis server and closes the connection pool.
     */
    public void disconnect() {
        if (pool != null && !pool.isClosed()) {
            pool.close();
        }
        connected = false;
    }

    /**
     * Checks if the client is connected to Redis.
     * 
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return connected && pool != null && !pool.isClosed();
    }

    // ==================== STRING OPERATIONS ====================

    /**
     * Sets a string value for the given key.
     * 
     * @param key   The key
     * @param value The value
     * @return true if successful, false otherwise
     */
    public boolean set(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            jedis.set(key, value);
            return true;
        } catch (JedisException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sets a string value with expiration time.
     * 
     * @param key     The key
     * @param value   The value
     * @param seconds Expiration time in seconds
     * @return true if successful, false otherwise
     */
    public boolean setex(String key, String value, long seconds) {
        try (Jedis jedis = pool.getResource()) {
            jedis.setex(key, seconds, value);
            return true;
        } catch (JedisException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets a string value for the given key.
     * 
     * @param key The key
     * @return The value, or null if not found
     */
    public String get(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(key);
        } catch (JedisException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Deletes one or more keys.
     * 
     * @param keys The keys to delete
     * @return The number of keys deleted
     */
    public long delete(String... keys) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.del(keys);
        } catch (JedisException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Checks if a key exists.
     * 
     * @param key The key
     * @return true if exists, false otherwise
     */
    public boolean exists(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists(key);
        } catch (JedisException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sets an expiration time on a key.
     * 
     * @param key     The key
     * @param seconds Time to live in seconds
     * @return true if successful, false otherwise
     */
    public boolean expire(String key, long seconds) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.expire(key, seconds) == 1;
        } catch (JedisException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets the remaining time to live of a key.
     * 
     * @param key The key
     * @return Time to live in seconds, -1 if no expiry, -2 if not found
     */
    public long ttl(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.ttl(key);
        } catch (JedisException e) {
            e.printStackTrace();
            return -2;
        }
    }

    // ==================== HASH OPERATIONS ====================

    /**
     * Sets a field value in a hash.
     * 
     * @param key   The hash key
     * @param field The field name
     * @param value The field value
     * @return true if successful, false otherwise
     */
    public boolean hset(String key, String field, String value) {
        try (Jedis jedis = pool.getResource()) {
            jedis.hset(key, field, value);
            return true;
        } catch (JedisException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets a field value from a hash.
     * 
     * @param key   The hash key
     * @param field The field name
     * @return The field value, or null if not found
     */
    public String hget(String key, String field) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hget(key, field);
        } catch (JedisException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets all fields and values from a hash.
     * 
     * @param key The hash key
     * @return A map of field-value pairs
     */
    public Map<String, String> hgetAll(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hgetAll(key);
        } catch (JedisException e) {
            e.printStackTrace();
            return Map.of();
        }
    }

    /**
     * Deletes one or more fields from a hash.
     * 
     * @param key    The hash key
     * @param fields The fields to delete
     * @return The number of fields deleted
     */
    public long hdel(String key, String... fields) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hdel(key, fields);
        } catch (JedisException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Checks if a field exists in a hash.
     * 
     * @param key   The hash key
     * @param field The field name
     * @return true if exists, false otherwise
     */
    public boolean hexists(String key, String field) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hexists(key, field);
        } catch (JedisException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==================== LIST OPERATIONS ====================

    /**
     * Pushes a value to the right (end) of a list.
     * 
     * @param key    The list key
     * @param values The values to push
     * @return The length of the list after push
     */
    public long rpush(String key, String... values) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.rpush(key, values);
        } catch (JedisException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Pushes a value to the left (start) of a list.
     * 
     * @param key    The list key
     * @param values The values to push
     * @return The length of the list after push
     */
    public long lpush(String key, String... values) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.lpush(key, values);
        } catch (JedisException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Gets a range of elements from a list.
     * 
     * @param key   The list key
     * @param start Start index (0-based)
     * @param end   End index (-1 for all)
     * @return List of values
     */
    public List<String> lrange(String key, long start, long end) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.lrange(key, start, end);
        } catch (JedisException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Gets the length of a list.
     * 
     * @param key The list key
     * @return The length of the list
     */
    public long llen(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.llen(key);
        } catch (JedisException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ==================== SET OPERATIONS ====================

    /**
     * Adds members to a set.
     * 
     * @param key     The set key
     * @param members The members to add
     * @return The number of members added
     */
    public long sadd(String key, String... members) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.sadd(key, members);
        } catch (JedisException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Gets all members of a set.
     * 
     * @param key The set key
     * @return Set of members
     */
    public Set<String> smembers(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.smembers(key);
        } catch (JedisException e) {
            e.printStackTrace();
            return Set.of();
        }
    }

    /**
     * Checks if a member exists in a set.
     * 
     * @param key    The set key
     * @param member The member to check
     * @return true if member exists, false otherwise
     */
    public boolean sismember(String key, String member) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.sismember(key, member);
        } catch (JedisException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Removes members from a set.
     * 
     * @param key     The set key
     * @param members The members to remove
     * @return The number of members removed
     */
    public long srem(String key, String... members) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.srem(key, members);
        } catch (JedisException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Increments a numeric value.
     * 
     * @param key The key
     * @return The new value after increment
     */
    public long incr(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.incr(key);
        } catch (JedisException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Increments a numeric value by a specific amount.
     * 
     * @param key       The key
     * @param increment The amount to increment
     * @return The new value after increment
     */
    public long incrBy(String key, long increment) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.incrBy(key, increment);
        } catch (JedisException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Decrements a numeric value.
     * 
     * @param key The key
     * @return The new value after decrement
     */
    public long decr(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.decr(key);
        } catch (JedisException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Decrements a numeric value by a specific amount.
     * 
     * @param key       The key
     * @param decrement The amount to decrement
     * @return The new value after decrement
     */
    public long decrBy(String key, long decrement) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.decrBy(key, decrement);
        } catch (JedisException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Gets all keys matching a pattern.
     * 
     * @param pattern The pattern (e.g., "player:*", "user:*:coins")
     * @return Set of matching keys
     */
    public Set<String> keys(String pattern) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.keys(pattern);
        } catch (JedisException e) {
            e.printStackTrace();
            return Set.of();
        }
    }

    /**
     * Executes a Redis PING command to test connectivity.
     * 
     * @return true if server responds with PONG, false otherwise
     */
    public boolean ping() {
        try (Jedis jedis = pool.getResource()) {
            return "PONG".equals(jedis.ping());
        } catch (JedisException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets direct access to the Jedis connection pool.
     * <p>
     * Use this for advanced operations not covered by the wrapper methods.
     * Remember to close resources properly using try-with-resources.
     * </p>
     * 
     * @return The Jedis connection pool
     */
    public JedisPool getPool() {
        return pool;
    }
}
