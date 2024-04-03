package dk.setups.celle.event.rent;

import dk.setups.celle.cell.Cell;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CellExpireEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final Cell cell;

    public CellExpireEvent(Cell cell) {
        this.cell = cell;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}