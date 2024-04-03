package dk.setups.celle.event.member;

import dk.setups.celle.cell.Cell;
import dk.setups.celle.cell.CellUser;
import dk.setups.celle.event.CellEvent;
import lombok.Getter;
import org.bukkit.event.HandlerList;

public class CellRemoveMemberEvent extends CellEvent {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final CellUser target;

    public CellRemoveMemberEvent(Cell cell, CellUser user, CellUser target) {
        super(cell, user);
        this.target = target;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
