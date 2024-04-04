package dk.setups.celle.database;

import com.j256.ormlite.dao.Dao;
import dk.setups.celle.sign.AvailableCellsGUISign;
import dk.setups.celle.sign.CellSign;
import org.bukkit.Location;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class AvailableCellGUISignStore extends BaseStore<Integer, AvailableCellsGUISign> {
    public AvailableCellGUISignStore(Dao<AvailableCellsGUISign, Integer> dao, StoreManager stores, Logger logger) {
        super(dao, stores, logger);
    }

    public List<AvailableCellsGUISign> getSignsInRegion(String regionName) {
        try {
            return getDao().queryForEq("worldguard_region_name", regionName);
        } catch (Exception e) {
            getLogger().warning("Failed to get signs in region " + regionName);
            e.printStackTrace();
            return null;
        }
    }

    public Optional<AvailableCellsGUISign> getSign(Location location) {
        try {
            return Optional.ofNullable(getDao().queryBuilder()
                    .where()
                    .eq("x", location.getBlockX())
                    .and()
                    .eq("y", location.getBlockY())
                    .and()
                    .eq("z", location.getBlockZ())
                    .and()
                    .eq("worldguard_region_world", location.getWorld().getName())
                    .queryForFirst()
            );
        } catch (Exception exception) {
            exception.printStackTrace();
            return Optional.empty();
        }
    }
}
