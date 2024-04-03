package dk.setups.celle.cell.log;

import dk.setups.celle.cell.Cell;
import dk.setups.celle.cell.CellUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString @EqualsAndHashCode
public class CellLogFilter {

    private final CellUser user;
    private final CellUser target;
    private boolean targetOrUser;
    private final Cell cell;
    private final LoggableAction action;

    public CellLogFilter(CellUser user) {
        this(user, null, false, null, null);
    }

    public CellLogFilter(Cell cell) {
        this(null, null, false, cell, null);
    }

    public CellLogFilter(CellUser user, CellUser target, boolean targetOrUser, Cell cell, LoggableAction action) {
        this.user = user;
        this.target = target;
        this.targetOrUser = targetOrUser;
        this.cell = cell;
        this.action = action;
    }

}
