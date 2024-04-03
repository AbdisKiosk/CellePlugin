package dk.setups.celle.cell.log;

import dk.setups.celle.cell.Cell;
import dk.setups.celle.cell.CellUser;
import lombok.Getter;

@Getter
public class CellLogFilterBuilder {

    private CellUser user;
    private CellUser target;
    private boolean targetOrUser;
    private Cell cell;
    private LoggableAction action;

    public static CellLogFilterBuilder of(CellLogFilter filter) {
        return new CellLogFilterBuilder()
                .user(filter.getUser())
                .target(filter.getTarget())
                .cell(filter.getCell())
                .action(filter.getAction());
    }

    public CellLogFilterBuilder user(CellUser user) {
        this.user = user;
        return this;
    }

    public CellLogFilterBuilder target(CellUser target) {
        this.target = target;
        return this;
    }

    public CellLogFilterBuilder cell(Cell cell) {
        this.cell = cell;
        return this;
    }

    public CellLogFilterBuilder action(LoggableAction action) {
        this.action = action;
        return this;
    }

    public CellLogFilterBuilder targetOrUser(boolean targetOrUser) {
        this.targetOrUser = targetOrUser;
        return this;
    }


    public CellLogFilter build() {
        return new CellLogFilter(user, target, targetOrUser, cell, action);
    }
}
