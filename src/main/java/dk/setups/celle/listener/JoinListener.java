package dk.setups.celle.listener;

import dk.setups.celle.cell.Cell;
import dk.setups.celle.cell.CellUser;
import dk.setups.celle.database.StoreManager;
import dk.setups.celle.util.PlayerSignDisallow;
import dk.setups.celle.util.cell.CellAPI;
import dk.setups.celle.util.cell.CellUtils;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@Component
public class JoinListener implements Listener {

    private @Inject Plugin plugin;
    private @Inject StoreManager stores;
    private @Inject PlayerSignDisallow disallow;
    private @Inject CellUtils utils;
    private @Inject CellAPI api;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        disallow.disallow(event.getPlayer().getUniqueId());
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->  {
            removeAsMemberFromNonPermittedCells(event.getPlayer());
        });
    }

    public void removeAsMemberFromNonPermittedCells(@NotNull Player player) {
        CellUser user = stores.getUserStore().get(player);
        if(user == null) {
            return;
        }
/*
        for(Cell addedCell : stores.getMemberStore().getAllowedCells(user)) {
            String memberPermission = addedCell.getGroup().getMemberPermission();
            if(player.hasPermission(memberPermission)) {
                addedCell.removeMember(user);
                utils.updateAndSave(addedCell);
            }
        }
  */  }
}
