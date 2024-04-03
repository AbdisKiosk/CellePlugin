package dk.setups.celle.util.cell;

import dk.setups.celle.cell.Cell;
import dk.setups.celle.cell.CellUser;
import dk.setups.celle.config.LangConfig;
import dk.setups.celle.database.StoreManager;
import dk.setups.celle.util.VaultUtils;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.bukkit.i18n.BI18n;
import eu.okaeri.platform.core.annotation.Component;
import eu.okaeri.tasker.core.Tasker;
import org.bukkit.entity.Player;

import java.util.Date;

@Component
public class CellRentManager {

    private @Inject("lang") BI18n i18n;
    private @Inject LangConfig lang;
    private @Inject StoreManager stores;
    private @Inject Tasker tasker;
    private @Inject CellUtils utils;
    private @Inject CellAPI api;
    private @Inject VaultUtils vault;


    public void handleCellUse(Player player, Cell cell) {
        if(cell.isRented() && cell.getOwner().getUuid().equals(player.getUniqueId())) {
            attemptExtendCell(player, cell);
            return;
        }
        attemptRentCell(player, cell);
    }

    public void attemptRentCell(Player player, Cell cell) {
        tasker.newChain()
                .abortIfSyncNot(() -> {
                    if(cell.isRented()) {
                        i18n.get(lang.getCellAttemptRentAlreadyRented()).with("cell", cell).sendTo(player);
                        return false;
                    }
                    return true;
                })
                .abortIfSyncNot(() -> {
                    if(!player.hasPermission(cell.getGroup().getRentPermission())) {
                        i18n.get(lang.getCellAttemptRentNoPermission()).with("cell", cell).sendTo(player);
                        return false;
                    }
                    return true;
                })
                .abortIfAsyncNot(() -> {
                    if(utils.hasRentedMax(player, cell.getGroup())) {
                            i18n.get(lang.getCellAttemptRentMaxRented()).with("cell", cell).sendTo(player);
                        return false;
                    }
                    return true;
                })
                .abortIfSyncNot(() -> {
                    if(!vault.tryTakeMoney(player, cell.getGroup().getRentPrice())) {
                        i18n.get(lang.getCellAttemptRentNotEnoughMoney()).with("group", cell).sendTo(player);
                        return false;
                    }
                    return true;
                })
                .async(() -> {
                    EventSuccess success = api.rentCell(cell, stores.getUserStore().get(player));
                    if(success == EventSuccess.FAILED) {
                        vault.addMoney(player, cell.getGroup().getRentPrice());
                        i18n.get(lang.getCellAttemptRentAlreadyRented())
                                .with("cell", cell.getName())
                                .with("group", cell.getGroup().getName())
                                .sendTo(player);
                        return;
                    }
                    if(success == EventSuccess.CANCELLED) {
                        vault.addMoney(player, cell.getGroup().getRentPrice());
                        return;
                    }
                    i18n.get(lang.getCellAttemptRentSuccess()).with("cell", cell).sendTo(player);
                    utils.update(cell);
                }).execute();
    }

    public void attemptExtendCell(Player player, Cell cell) {
        tasker.newChain()
                .abortIfSyncNot(() -> {
                     if(cell.getOwner().getUuid().equals(player.getUniqueId()) && cell.isRented()) {
                         return true;
                     }
                    i18n.get(lang.getCellAttemptExtendNotOwned()).with("cell", cell).sendTo(player);
                     return false;
                })
                .abortIfAsyncNot(() -> {
                    if(!cell.canExtend()) {
                        i18n.get(lang.getCellAttemptExtendFullyExtended()).with("cell", cell).sendTo(player);
                        return false;
                    }
                    return true;
                })
                .abortIfSyncNot(() -> {
                    if(!vault.tryTakeMoney(player, cell.getGroup().getRentPrice())) {
                        i18n.get(lang.getCellAttemptExtendNotEnoughMoney()).with("cell", cell).sendTo(player);
                        return false;
                    }
                    return true;
                })
                .async(() -> {
                    if(api.extendCell(cell, stores.getUserStore().get(player)) == EventSuccess.CANCELLED) {
                        vault.addMoney(player, cell.getGroup().getRentPrice());
                        return;
                    }
                    i18n.get(lang.getCellAttemptExtendSuccess()).with("cell", cell).sendTo(player);
                }).execute();
    }
}
