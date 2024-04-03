package dk.setups.celle.event;

import dk.setups.celle.cell.Cell;
import dk.setups.celle.cell.CellUser;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CellEvent extends PlayerEvent implements Cancellable {

    @Getter
    private final Cell cell;
    @Getter
    private final CellUser user;

    @Getter
    @Setter
    private boolean cancelled;

    public CellEvent(Cell cell, CellUser user) {
        this(cell, user, Bukkit.getPlayer(user.getUuid()));
    }

    public CellEvent(Cell cell, CellUser user, Player player) {
        super(player);
        this.cell = cell;
        this.user = user;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
