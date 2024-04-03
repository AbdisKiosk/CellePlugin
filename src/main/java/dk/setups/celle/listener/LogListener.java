package dk.setups.celle.listener;

import dk.setups.celle.cell.Cell;
import dk.setups.celle.cell.CellUser;
import dk.setups.celle.cell.log.CellLog;
import dk.setups.celle.cell.log.LoggableAction;
import dk.setups.celle.database.CellLogStore;
import dk.setups.celle.database.StoreManager;
import dk.setups.celle.event.member.CellAddMemberEvent;
import dk.setups.celle.event.member.CellRemoveMemberEvent;
import dk.setups.celle.event.rent.CellExpireEvent;
import dk.setups.celle.event.rent.CellExtendEvent;
import dk.setups.celle.event.rent.CellRentEvent;
import dk.setups.celle.event.rent.CellUnrentEvent;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@Component
public class LogListener implements Listener {

    private @Inject StoreManager stores;

    @EventHandler
    public void onCellRentEvent(CellRentEvent event) {
        log(LoggableAction.RENT, event.getCell(), event.getUser());
    }

    @EventHandler
    public void onCellUnrentEvent(CellUnrentEvent event) {
        log(LoggableAction.UNRENT, event.getCell(), event.getUser());
    }

    @EventHandler
    public void onCellExpireEvent(CellExpireEvent event) {
        log(LoggableAction.EXPIRE, event.getCell());
    }

    @EventHandler
    public void onCellExtendEvent(CellExtendEvent event) {
        log(LoggableAction.EXTEND, event.getCell(), event.getUser());
    }

    @EventHandler
    public void onCellAddMemberEvent(CellAddMemberEvent event) {
        log(LoggableAction.MEMBER_ADD, event.getCell(), event.getUser(), event.getTarget());
    }

    @EventHandler
    public void onCellRemoveMemberEvent(CellRemoveMemberEvent event) {
        log(LoggableAction.MEMBER_REMOVE, event.getCell(), event.getUser(), event.getTarget());
    }


    protected void log(LoggableAction action, Cell cell) {
        this.log(action, cell, null, null);
    }

    protected void log(LoggableAction action, Cell cell, CellUser user) {
        this.log(action, cell, user, null);
    }

    protected void log(LoggableAction action, Cell cell, CellUser user, CellUser target) {
        CellLogStore store = this.stores.getLogStore();
        store.persist(new CellLog(action, cell, user, target));
    }

}
