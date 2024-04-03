package dk.setups.celle.database;

import com.j256.ormlite.dao.Dao;
import dk.setups.celle.cell.CellRegion;

import java.util.logging.Logger;

public class RegionStore extends BaseStore<String, CellRegion> {

    public RegionStore(Dao<CellRegion, String> dao, StoreManager stores, Logger logger) {
        super(dao, stores, logger);
    }
}
