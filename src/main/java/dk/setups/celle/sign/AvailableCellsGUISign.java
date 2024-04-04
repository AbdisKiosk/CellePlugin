package dk.setups.celle.sign;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dk.setups.celle.util.WorldGuardUtils;
import lombok.Getter;
import org.bukkit.Bukkit;

@DatabaseTable(tableName = AvailableCellsGUISign.TABLE_NAME)
public class AvailableCellsGUISign {

    public static final String TABLE_NAME = "available_cells_gui_signs";

    @Getter
    @DatabaseField(generatedId = true)
    private int id;

    @Getter
    @DatabaseField(canBeNull = false, columnName = "x", uniqueCombo = true)
    private int x;

    @Getter
    @DatabaseField(canBeNull = false, columnName = "y", uniqueCombo = true)
    private int y;

    @Getter
    @DatabaseField(canBeNull = false, columnName = "z", uniqueCombo = true)
    private int z;

    @Getter
    @DatabaseField(canBeNull = false, columnName = "worldguard_region_name")
    private String regionName;
    @Getter
    @DatabaseField(canBeNull = false, columnName = "worldguard_region_world", uniqueCombo = true)
    private String world;

    public AvailableCellsGUISign(int x, int y, int z, String regionName, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.regionName = regionName;
        this.world = world;
    }

    public ProtectedRegion getRegion(WorldGuardUtils worldGuard) {
        return worldGuard.getRegionByName(Bukkit.getWorld(world), regionName)
                .orElseThrow(() -> new IllegalStateException("Region not found"));
    }

    @SuppressWarnings("unused")
    AvailableCellsGUISign() {
    }
}
