package dk.setups.celle.database;

import com.j256.ormlite.dao.Dao;
import dk.setups.celle.sign.AvailableCellsSign;

import java.util.List;
import java.util.logging.Logger;

public class AvailableCellSignStore extends BaseStore<Integer, AvailableCellsSign> {

    public AvailableCellSignStore(Dao<AvailableCellsSign, Integer> dao, StoreManager stores, Logger logger) {
        super(dao, stores, logger);
    }

    public List<AvailableCellsSign> getSignsInRegion(String regionName) {
        try {
            return getDao().queryForEq("worldguard_region_name", regionName);
        } catch (Exception e) {
            getLogger().warning("Failed to get signs in region " + regionName);
            e.printStackTrace();
            return null;
        }
    }
}
