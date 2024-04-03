package dk.setups.celle.database;

import com.j256.ormlite.dao.Dao;
import dk.setups.celle.cell.CellTeleport;

import java.util.logging.Logger;

public class CellTeleportStore extends BaseStore<Integer, CellTeleport>{

    public CellTeleportStore(Dao<CellTeleport, Integer> dao, StoreManager stores, Logger logger) {
        super(dao, stores, logger);
    }

}
