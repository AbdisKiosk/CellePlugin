package dk.setups.celle.cell;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data @EqualsAndHashCode(callSuper = false)
@DatabaseTable(tableName = CellUser.TABLE_NAME)
public class CellUser {

    public static final String TABLE_NAME = "cell_users";

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(canBeNull = false, uniqueIndex = true, columnName = "mc_uuid")
    private UUID uuid;
    @DatabaseField(canBeNull = false, index = true, columnName = "mc_name")
    private String name;

    public CellUser(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    CellUser() {
    }
}
