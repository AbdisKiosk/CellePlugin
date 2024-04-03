package dk.setups.celle.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import dk.setups.celle.cell.log.CellLog;
import dk.setups.celle.cell.log.CellLogFilter;
import org.bukkit.Bukkit;

import javax.swing.plaf.basic.BasicButtonUI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CellLogStore extends BaseStore<Integer, CellLog> {

    public CellLogStore(Dao<CellLog, Integer> dao, StoreManager stores, Logger logger) {
        super(dao, stores, logger);
    }

    public List<CellLog> getLogs(CellLogFilter filter, int limit, int skip) {
        try {
            QueryBuilder<CellLog, Integer> query = getDao().queryBuilder();
            if(filter.getUser() != null) {
                Where<?, ?> where = query.where().eq("actor_id", filter.getUser().getId());
                if(filter.isTargetOrUser()) {
                    where.or().eq("target_id", filter.getUser().getId());
                }
            }
            if(filter.getTarget() != null) {
                Where<?, ?> where = query.where().eq("target_id", filter.getTarget().getId());
                if(filter.isTargetOrUser()) {
                    where.or().eq("actor_id", filter.getTarget().getId());
                }
            }
            if(filter.getCell() != null) {
                query.where().eq("cell_id", filter.getCell().getId());
            }
            if(filter.getAction() != null) {
                query.where().eq("action", filter.getAction());
            }

            query.orderBy("id", false);
            query.offset((long) skip).limit((long) limit);

            return query.query();
        } catch(SQLException exception) {
            getLogger().log(Level.FINE, "Failed to get logs for filter: " + filter, exception);
            return new ArrayList<>();
        }
    }
}