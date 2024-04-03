package dk.setups.celle.cell;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper = false)
@DatabaseTable(tableName = CellRegion.TABLE_NAME)
public class CellRegion {

    public static final String TABLE_NAME = "cell_regions";

    @DatabaseField(id = true, columnName = "worldguard_region_name")
    private String name;
    @DatabaseField(canBeNull = false, columnName = "worldguard_region_world")
    private String regionWorld;

    public CellRegion(String name, String regionWorld) {
        this.name = name;
        this.regionWorld = regionWorld;
    }

    CellRegion() {
    }
}
