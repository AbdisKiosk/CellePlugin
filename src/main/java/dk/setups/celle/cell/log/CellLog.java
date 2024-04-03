package dk.setups.celle.cell.log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dk.setups.celle.cell.Cell;
import dk.setups.celle.cell.CellUser;
import lombok.*;

import java.util.Date;

@EqualsAndHashCode @Getter @ToString
@DatabaseTable(tableName = CellLog  .TABLE_NAME)
public class CellLog {

    public static final String TABLE_NAME = "cell_logs";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, columnName = "action")
    private LoggableAction action;

    @DatabaseField(canBeNull = false, columnName = "timestamp")
    private Date timestamp;

    @DatabaseField(canBeNull = false, columnName = "cell_id", foreign = true, foreignAutoRefresh = true, index = true)
    private Cell cell;

    @DatabaseField(canBeNull = true, columnName = "actor_id", foreign = true, foreignAutoRefresh = true, index = true)
    private CellUser actor;

    @DatabaseField(canBeNull = true, columnName = "target_id", foreign = true, foreignAutoRefresh = true, index = true)
    private CellUser target;

    public CellLog(LoggableAction action, Cell cell) {
        this.action = action;
        this.timestamp = new Date();
        this.cell = cell;
    }

    public CellLog(LoggableAction action, Cell cell, CellUser actor) {
        this.action = action;
        this.timestamp = new Date();
        this.cell = cell;
        this.actor = actor;
    }

    public CellLog(LoggableAction action, Cell cell, CellUser actor, CellUser target) {
        this.action = action;
        this.timestamp = new Date();
        this.cell = cell;
        this.actor = actor;
        this.target = target;
    }

    CellLog() {
    }

}
