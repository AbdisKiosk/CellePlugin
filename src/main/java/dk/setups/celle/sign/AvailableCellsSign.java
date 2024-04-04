package dk.setups.celle.sign;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;

@DatabaseTable(tableName = AvailableCellsSign.TABLE_NAME)
public class AvailableCellsSign {

    public static final String TABLE_NAME = "available_cells_signs";

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
    @DatabaseField(canBeNull = false, columnName = "sign_index")
    private int signIndex;

    @Getter
    @DatabaseField(canBeNull = false, columnName = "worldguard_region_name")
    private String regionName;
    @Getter
    @DatabaseField(canBeNull = false, columnName = "worldguard_region_world", uniqueCombo = true)
    private String world;

    @SuppressWarnings("unused")
    AvailableCellsSign() {
    }

}