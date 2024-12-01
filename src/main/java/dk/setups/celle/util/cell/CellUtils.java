package dk.setups.celle.util.cell;

import dk.setups.celle.cell.Cell;
import dk.setups.celle.cell.CellGroup;
import dk.setups.celle.config.Config;
import dk.setups.celle.database.StoreManager;
import dk.setups.celle.util.NearbyPlayerMap;
import dk.setups.celle.util.PlayerSignDisallow;
import dk.setups.celle.util.SignContentCreator;
import dk.setups.celle.util.WorldGuardUtils;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

@Component
public class CellUtils {

    private @Inject SignContentCreator content;
    private @Inject StoreManager stores;
    private @Inject WorldGuardUtils worldGuard;
    private @Inject Config config;
    private @Inject PlayerSignDisallow disallow;
    private @Inject JavaPlugin plugin;

    public void updateAndSave(Cell cell) {
        stores.getCellStore().persist(cell);
        update(cell);
    }

    public void update(Cell cell) {
        try {
            Bukkit.getScheduler().runTask(plugin, () -> {
                worldGuard.updateRegion(cell);
            });
        } catch(Exception exception) {
            Bukkit.getLogger().warning("Failed to update region for cell: " + cell.getName());
            exception.printStackTrace();
        }
        updateSign(cell);
    }

    public void updateSign(Cell cell) {
        updateSign(NearbyPlayerMap.from(disallow.filter(Bukkit.getOnlinePlayers())), cell);
    }

    @SuppressWarnings("deprecation")
    public void updateSign(NearbyPlayerMap nearby, Cell cell) {
        if(cell.getSign() == null) {
            return;
        }
        long SECOND = 1000;
        for(Player player : nearby.getNearbyPlayers(cell.getSign().getLocation())) {
            long timeSinceLastJoin = System.currentTimeMillis() - player.getLastPlayed();
            if(timeSinceLastJoin < SECOND * 5) {
                continue;                                                                   
            }
            Location sign = cell.getSign().getLocation();
            player.sendBlockChange(sign, Material.WALL_SIGN, sign.getBlock().getData());

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.sendSignChange(sign, content.getSignContent(cell, player));
            }, 1);
        }
    }

    public boolean hasRentedMax(Player player, CellGroup group) {
        Collection<Cell> cells = stores.getCellStore().getOwnedCells(stores.getUserStore().get(player));
        if(cells.size() >= getGlobalMaxCells(player)) {
            return true;
        }
        int cellsInGroup = (int) cells.stream().filter(cell -> cell.getGroup().equals(group)).count();
        return cellsInGroup >= group.getMaxRentedCells();
    }

    public int getGlobalMaxCells(Player player) {
        return config.getMaxCellsPerPlayer().keySet().stream()
                .filter(permission -> player.hasPermission(permission) || permission.equals("default"))
                .findFirst()
                .map(config.getMaxCellsPerPlayer()::get)
                .orElse(0);
    }

}
