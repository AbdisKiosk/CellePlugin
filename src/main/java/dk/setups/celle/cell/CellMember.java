package dk.setups.celle.cell;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper = false)
@DatabaseTable(tableName = CellMember.TABLE_NAME)
public class CellMember {

    public static final String TABLE_NAME = "cell_members";

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, index = true, columnName = "user_id")
    private CellUser user;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, index = true, columnName = "cell_id")
    private Cell cell;

    public CellMember(CellUser user) {
        this.user = user;
    }

    CellMember() {
    }
}
