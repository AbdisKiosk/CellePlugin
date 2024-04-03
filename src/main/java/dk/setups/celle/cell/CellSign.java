package dk.setups.celle.cell;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Data @EqualsAndHashCode(callSuper = false)
@DatabaseTable(tableName = CellSign.TABLE_NAME)
public class CellSign {

    public static final String TABLE_NAME = "cell_signs";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, columnName = "x", uniqueCombo = true)
    private int x;
    @DatabaseField(canBeNull = false, columnName = "y", uniqueCombo = true)
    private int y;
    @DatabaseField(canBeNull = false, columnName = "z", uniqueCombo = true)
    private int z;
    @DatabaseField(canBeNull = false, columnName = "world", uniqueCombo = true)
    private String world;

    public CellSign(Location location) {
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.world = location.getWorld().getName();
    }

    public CellSign(int x, int y, int z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    CellSign() {
    }
}
