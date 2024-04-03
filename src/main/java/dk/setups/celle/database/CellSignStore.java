package dk.setups.celle.database;

import com.j256.ormlite.dao.Dao;
import dk.setups.celle.cell.Cell;
import dk.setups.celle.cell.CellSign;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.Location;

import java.util.Optional;
import java.util.logging.Logger;

public class CellSignStore extends BaseStore<Integer, CellSign> {

    public CellSignStore(Dao<CellSign, Integer> dao, StoreManager stores, Logger logger) {
        super(dao, stores, logger);
    }

}
