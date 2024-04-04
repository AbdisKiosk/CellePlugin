package dk.setups.celle.database;

import com.j256.ormlite.dao.Dao;
import dk.setups.celle.sign.CellSign;

import java.util.logging.Logger;

public class CellSignStore extends BaseStore<Integer, CellSign> {

    public CellSignStore(Dao<CellSign, Integer> dao, StoreManager stores, Logger logger) {
        super(dao, stores, logger);
    }

}
