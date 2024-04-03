package dk.setups.celle.migrate.staxi;

import dk.setups.celle.database.StoreManager;
import dk.setups.celle.migrate.CellMigrator;
import dk.setups.celle.migrate.MigrateCell;
import dk.setups.celle.util.cell.CellFactory;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class StaxiCellMigrator extends CellMigrator {

    public StaxiCellMigrator(StoreManager stores, CellFactory factory, File folder) {
        super(stores, factory, folder);
    }

    @Override
    public Set<MigrateCell> getCells() {
        Set<MigrateCell> migrateCells = new HashSet<>();

        File file = new File(getFolder(), "Celler");
        for(File cellFile : file.listFiles()) {
            StaxiCell cell = StaxiCell.from(YamlConfiguration.loadConfiguration(cellFile));
            migrateCells.add(cell.toCell(cellFile.getName().replace(".yml", "")));
        }
        return migrateCells;
    }
}
