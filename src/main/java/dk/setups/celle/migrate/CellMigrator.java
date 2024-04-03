package dk.setups.celle.migrate;

import dk.setups.celle.cell.Cell;
import dk.setups.celle.cell.CellGroup;
import dk.setups.celle.cell.CellUser;
import dk.setups.celle.database.StoreManager;
import dk.setups.celle.util.cell.CellFactory;
import lombok.Getter;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public abstract class CellMigrator {

    @Getter
    private final StoreManager stores;
    @Getter
    private final CellFactory factory;
    @Getter
    private final File folder;

    public CellMigrator(StoreManager stores, CellFactory factory, File folder) {
        this.stores = stores;
        this.folder = folder;
        this.factory = factory;
    }

    public void migrate() {
        Set<MigrateCell> cells = getCells();

        Map<MigrateCellGroup, Set<MigrateCell>> groupToCells = new HashMap<>();
        for(MigrateCell cell : cells) {
            MigrateCellGroup group = cell.getGroup();
            if(group != null) {
                groupToCells.computeIfAbsent(group, k -> new HashSet<>()).add(cell);
            }
        }

        Map<MigrateCellGroup, CellGroup> migratedGroups = new HashMap<>();
        for(MigrateCellGroup toMigrate : groupToCells.keySet()) {
            String name = "Generated" + new Random().nextInt();
            migratedGroups.put(toMigrate, toMigrate.toGroup(name, factory));
        }

        Set<Cell> migratedCells = new HashSet<>();
        for(MigrateCell cell : cells) {
            CellGroup group = migratedGroups.get(cell.getGroup());
            migratedCells.add(cell.toCell(group, stores.getUserStore()));
        }

        migratedGroups.values().forEach(stores.getGroupStore()::persist);
        migratedCells.forEach(this::persistCell);
    }

    private CellUser convert(MigrateUser user) {
        return stores.getUserStore().get(user.getUuid(), user.getName());
    }


    private void persistCell(Cell cell) {
        stores.getRegionStore().persist(cell.getRegion());
        stores.getSignStore().persist(cell.getSign());
        stores.getUserStore().persist(cell.getOwner());
        stores.getCellStore().persist(cell);
    }

    public Set<MigrateUser> getUsers() {
        Set<UUID> uuids = new HashSet<>();
        for(MigrateCell cell : getCells()) {
            if(cell.getOwner() != null) {
                uuids.add(cell.getOwner());
            }
            uuids.addAll(cell.getMembers());
        }
        return uuids.stream()
                .map(MigrateUser::fromUUID)
                .collect(Collectors.toSet());
    }

    /**
     * When this is called, users and grups have been added to datastore
     */
    public abstract Set<MigrateCell> getCells();


}
