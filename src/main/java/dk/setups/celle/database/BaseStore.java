package dk.setups.celle.database;

import com.j256.ormlite.dao.Dao;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseStore<K, V> {

    @Getter
    private Dao<V, K> dao;
    @Getter
    private StoreManager stores;
    @Getter
    private Logger logger;

    public BaseStore(Dao<V, K> dao, StoreManager stores, Logger logger) {
        this.dao = dao;
        this.stores = stores;
        this.logger = logger;
    }

    public V getOrPersist(String key, Object keyValue, V createIfAbsent) {
        Optional<V> existing = this.get(key, keyValue);
        if(existing.isPresent()) {
            return existing.get();
        }
        this.persist(createIfAbsent);
        return createIfAbsent;
    }

    public void persist(V value) {
        try {
            this.dao.createOrUpdate(value);
        } catch (Exception exception) {
            logger.log(Level.SEVERE, "Failed to create/update " + value.toString(), exception);
        }
    }

    public Optional<V> get(String key, Object keyValue) {
        return getAll(key, keyValue).stream().findAny();
    }

    public List<V> getAll(String key, Object keyValue) {
        try {
            return this.dao.queryForEq(key, keyValue);
        } catch(Exception exception) {
            logger.log(Level.SEVERE, "Failed to get object with key " + key + " = " + keyValue, exception);
        }
        return Collections.emptyList();
    }

    public List<V> getAll() {
        try {
            return this.dao.queryForAll();
        } catch(Exception exception) {
            logger.log(Level.SEVERE, "Failed to get all objects", exception);
        }
        return Collections.emptyList();
    }

    public Optional<V> get(K key) {
        try {
            return Optional.ofNullable(this.dao.queryForId(key));
        } catch (Exception exception) {
            logger.log(Level.SEVERE, "Failed to get " + key, exception);
        }
        return Optional.empty();
    }


    public void delete(K key) {
        try {
            this.dao.deleteById(key);
        } catch (Exception exception) {
            logger.log(Level.SEVERE, "Failed to delete " + key, exception);
        }
    }

}