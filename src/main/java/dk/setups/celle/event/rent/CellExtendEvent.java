package dk.setups.celle.event.rent;

import dk.setups.celle.cell.Cell;
import dk.setups.celle.cell.CellUser;
import dk.setups.celle.event.CellEvent;
import org.bukkit.event.HandlerList;

public class CellExtendEvent extends CellEvent {

    private static final HandlerList handlers = new HandlerList();

    public CellExtendEvent(Cell cell, CellUser user) {
        super(cell, user);
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
