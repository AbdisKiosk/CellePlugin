package dk.setups.celle.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dk.setups.celle.cell.Cell;
import dk.setups.celle.cell.CellMember;
import dk.setups.celle.sign.CellSign;
import dk.setups.celle.cell.CellUser;
import dk.setups.celle.util.cell.CellUtils;
import eu.okaeri.injector.annotation.Inject;
import lombok.Getter;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CellStore extends BaseStore<Integer, Cell> {

    @Getter
    private final Cache<Integer, Cell> cache = new Cache<>();
    private @Inject CellUtils utils;

    public CellStore(Dao<Cell, Integer> dao, StoreManager stores, Logger logger) {
        super(dao, stores, logger);
        updateCache();
    }

    public void updateCache() {
        try {
            for (Cell cell : getDao().queryForAll()) {
                cache.update(cell.getId(), cell);
            }
        } catch (Exception exception) {
            getLogger().log(Level.SEVERE, "Failed to fill cache", exception);
        }
    }

    @Override
    public void persist(Cell cell) {
        cache.update(cell.getId(), cell);
        super.persist(cell);
    }

    @Override
    public void delete(Integer id) {
        cache.invalidate(id);

        try {
            getDao().callBatchTasks(() -> {
                Cell cell = get(id).orElse(null);
                if (cell == null) {
                    return null;
                }
                if (cell.getMembers() != null) {
                    cell.getMembers().forEach(member -> {
                        getStores().getMemberStore().delete(member.getId());
                    });
                }

                if (cell.getSign() != null) {
                    getStores().getSignStore().delete(cell.getSign().getId());
                }

                if (cell.getRegion() != null) {
                    getStores().getRegionStore().delete(cell.getRegion().getName());
                }
                if(cell.getTeleport() != null) {
                    getStores().getTeleportStore().delete(cell.getTeleport().getId());
                }

                super.delete(id);

                return null;
            });
        } catch(Exception ex) {
            getLogger().log(Level.SEVERE, "Failed to delete cell " + id, ex);
        }
    }

    //TODO: Change this to database-level synchronization
    public synchronized boolean tryChangeOwner(Cell cell, CellUser newOwner, Date ownerUntil) {
        try {
            return getDao().callBatchTasks(() -> {
                Cell loaded = getDao().queryForId(cell.getId());
                if (loaded == null || loaded.isRented()) {
                    return false;
                }
                loaded.setOwner(newOwner);
                loaded.setRentedUntil(ownerUntil);
                loaded.clearMembers();
                persist(loaded);
                return true;
            });
        } catch (Exception exception) {
            getLogger().log(Level.SEVERE,
                    "Failed to change owner of cell " + cell.getName() + " to " + newOwner.getName(), exception);
        }
        return false;
    }

    public Collection<Cell> getOwnedCells(CellUser user) {
        try {
            return getDao().queryForEq("owner_id", user.getId())
                    .stream()
                    .filter(Cell::isRented)
                    .collect(Collectors.toList());
        } catch (Exception exception) {
            getLogger().log(Level.SEVERE, "Failed to get owned cells for " + user.getName(), exception);
        }
        return Collections.emptySet();
    }

    public Collection<Cell> getPermittedCells(CellUser user) {
        try {
            QueryBuilder<CellMember, Integer> memberQuery = getStores().getMemberStore().getDao().queryBuilder()
                    .where().eq("user_id", user.getId()).queryBuilder();

            return getDao().queryBuilder()
                    .where().eq("owner_id", user.getId())
                    .or().in("id", memberQuery.selectColumns("cell_id"))
                    .query()
                    .stream()
                    .filter(Cell::isRented)
                    .collect(Collectors.toList());
        } catch (SQLException ex) {
            getLogger().log(Level.SEVERE, "Failed to get permitted cells for " + user.getName(), ex);
        }
        return Collections.emptySet();
    }

    public Collection<Cell> getCellsInRegions(Collection<ProtectedRegion> regions) {
        try {
            Set<String> names = regions.stream().map(ProtectedRegion::getId).collect(Collectors.toSet());
            return getDao().queryBuilder()
                    .join(getStores().getRegionStore().getDao().queryBuilder()
                            .where().in("worldguard_region_name", names).queryBuilder())
                    .query();
        } catch(SQLException ex) {
            getLogger().log(Level.SEVERE, "Failed to get cells in regions", ex);
        }
        return Collections.emptySet();
    }

    public Optional<Cell> getFromName(String name) {
        try {
            return Optional.ofNullable(getDao().queryBuilder().where()
                    .like("name", name)
                    .queryForFirst());
        } catch(Exception exception) {
            getLogger().log(Level.SEVERE, "Failed to get cell from name " + name, exception);
        }
        return Optional.empty();
    }

    public Optional<Cell> getFromSignLoc(int x, int y, int z, String world) {
        try {
            QueryBuilder<CellSign, Integer> signQuery = getStores().getSignStore().getDao().queryBuilder().where()
                    .eq("x", x).and()
                    .eq("y", y).and()
                    .eq("z", z).and()
                    .eq("world", world)
                    .queryBuilder();

            QueryBuilder<Cell, Integer> query = getDao().queryBuilder().join(signQuery);
            return Optional.ofNullable(query.queryForFirst());
        } catch(SQLException ex) {
            getLogger().log(Level.SEVERE, "Failed to get cell from sign location", ex);
        }
        return Optional.empty();
    }

    public Optional<Cell> getFromRegion(String regionName, String worldName) {
        try {
            return Optional.ofNullable(getDao().queryBuilder().join(getStores().getRegionStore().getDao().queryBuilder()
                    .where().eq("worldguard_region_name", regionName)
                            .and().eq("worldguard_region_world", worldName).queryBuilder())
                    .queryForFirst());
        } catch(SQLException ex) {
            getLogger().log(Level.SEVERE, "Failed to get cell from region", ex);
        }
        return Optional.empty();
    }

}