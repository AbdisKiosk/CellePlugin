package dk.setups.celle.cell;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

@Data @EqualsAndHashCode(callSuper = false)
@DatabaseTable(tableName = CellTeleport.TABLE_NAME)
public class CellTeleport {

    public static final String TABLE_NAME = "cell_teleports";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, columnName = "x", uniqueCombo = true)
    private int x;
    @DatabaseField(canBeNull = false, columnName = "y", uniqueCombo = true)
    private int y;
    @DatabaseField(canBeNull = false, columnName = "z", uniqueCombo = true)
    private int z;
    @DatabaseField(canBeNull = false, columnName = "yaw", uniqueCombo = true)
    private float yaw;
    @DatabaseField(canBeNull = false, columnName = "pitch", uniqueCombo = true)
    private float pitch;
    @DatabaseField(canBeNull = false, columnName = "world", uniqueCombo = true)
    private String world;


    public CellTeleport(int x, int y, int z, float yaw, float pitch, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }

    CellTeleport() {
    }

    public Location asBukkit() {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

}
