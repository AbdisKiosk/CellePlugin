package dk.setups.celle.database;

import com.j256.ormlite.dao.Dao;
import dk.setups.celle.cell.CellGroup;

import java.util.Optional;
import java.util.logging.Logger;

public class CellGroupStore extends BaseStore<Integer, CellGroup> {

    public CellGroupStore(Dao<CellGroup, Integer> dao, StoreManager stores, Logger logger) {
        super(dao, stores, logger);
    }

    public Optional<CellGroup> getFromName(String name) {
        return get("name", name);
    }
}