package dk.setups.celle.listener;

import dk.setups.celle.cell.Cell;
import dk.setups.celle.cell.CellTeleport;
import dk.setups.celle.cell.CellUser;
import dk.setups.celle.config.Config;
import dk.setups.celle.database.StoreManager;
import dk.setups.celle.util.PlayerSignDisallow;
import dk.setups.celle.util.cell.CellAPI;
import dk.setups.celle.util.cell.CellUtils;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
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
    private @Inject Config config;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        disallow.disallow(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void teleportOutOfCell(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location loginLocation = event.getPlayer().getLocation();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
             api.getCellAtLocation(loginLocation).ifPresent(cell -> {
                 if(cell.isPermitted(player.getUniqueId())) {
                     return;
                 }
                 Bukkit.getScheduler().runTask(plugin, () -> {
                     CellTeleport teleport = cell.getTeleport();
                     if(teleport == null) {
                         return;
                     }
                     player.teleport(teleport.asBukkit());
                 });
             });
        });
    }

}
