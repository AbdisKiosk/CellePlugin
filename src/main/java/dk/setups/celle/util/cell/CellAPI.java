package dk.setups.celle.util.cell;

import dk.setups.celle.cell.Cell;
import dk.setups.celle.cell.CellUser;
import dk.setups.celle.database.CellStore;
import dk.setups.celle.database.StoreManager;
import dk.setups.celle.event.member.CellAddMemberEvent;
import dk.setups.celle.event.member.CellRemoveMemberEvent;
import dk.setups.celle.event.rent.CellExpireEvent;
import dk.setups.celle.event.rent.CellExtendEvent;
import dk.setups.celle.event.rent.CellRentEvent;
import dk.setups.celle.event.rent.CellUnrentEvent;
import dk.setups.celle.util.WorldGuardUtils;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class CellAPI {

    private @Inject CellUtils utils;
    private @Inject StoreManager stores;
    private @Inject PluginManager pluginManager;
    private @Inject WorldGuardUtils worldGuard;

    public EventSuccess extendCell(Cell cell, CellUser user) {
        if (this.callEvent(new CellExtendEvent(cell, user))) {
            return EventSuccess.CANCELLED;
        }
        cell.extend();
        utils.updateAndSave(cell);
        return EventSuccess.SUCCESS;
    }

    public EventSuccess rentCell(Cell cell, CellUser user) {
        if (this.callEvent(new CellRentEvent(cell, user))) {
            return EventSuccess.CANCELLED;
        }
        Date rentUntil = new Date(System.currentTimeMillis() + cell.getGroup().getRentTimeMillis());
        if(stores.getCellStore().tryChangeOwner(cell, user, rentUntil)) {
            utils.update(cell);
            return EventSuccess.SUCCESS;
        }
        return EventSuccess.FAILED;
    }

    public EventSuccess unrentCell(Cell cell, CellUser user) {
        if (this.callEvent(new CellUnrentEvent(cell, user))) {
            return EventSuccess.CANCELLED;
        }
        cell.unrent();
        utils.updateAndSave(cell);
        return EventSuccess.SUCCESS;
    }

    public EventSuccess addMember(Cell cell, CellUser user, OfflinePlayer target) {
        return addMember(cell, user, stores.getUserStore().get(target));
    }

    public EventSuccess addMember(Cell cell, CellUser user, CellUser target) {
        if(this.callEvent(new CellAddMemberEvent(cell, user, target))) {
            return EventSuccess.CANCELLED;
        }
        cell.addMember(target);
        utils.updateAndSave(cell);
        return EventSuccess.SUCCESS;
    }

    public EventSuccess removeMember(Cell cell, CellUser user, CellUser target) {
        if(this.callEvent(new CellRemoveMemberEvent(cell, user, target))) {
            return EventSuccess.CANCELLED;
        }
        cell.removeMember(target);
        utils.updateAndSave(cell);
        return EventSuccess.SUCCESS;
    }

    public void expireCell(Cell cell) {
        callEvent(new CellExpireEvent(cell));
        cell.unrent();
        utils.updateAndSave(cell);
    }

    public Optional<Cell> getCellAtLocation(Location location) {
        return worldGuard.getHighestPriority(location)
                .flatMap(region -> stores.getCellStore().getFromRegion(region.getId(), location.getWorld().getName()));
    }

    private boolean callEvent(Event event) {
        pluginManager.callEvent(event);
        return event instanceof Cancellable && ((Cancellable) event).isCancelled();
    }
}
