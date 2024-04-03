package dk.setups.celle.database;

import com.j256.ormlite.dao.Dao;
import dk.setups.celle.cell.Cell;
import dk.setups.celle.cell.CellMember;
import dk.setups.celle.cell.CellUser;

import java.util.Collection;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CellMemberStore extends BaseStore<Integer, CellMember> {

    public CellMemberStore(Dao<CellMember, Integer> dao, StoreManager stores, Logger logger) {
        super(dao, stores, logger);
    }

    public Collection<Cell> getAllowedCells(CellUser user) {
        return getAll("user_id", user.getId())
                .stream()
                .map(CellMember::getCell)
                .filter(Cell::isRented)
                .collect(Collectors.toSet());
    }

}
