package dk.setups.celle.task;

import dk.setups.celle.cell.Cell;
import dk.setups.celle.database.StoreManager;
import dk.setups.celle.util.cell.CellAPI;
import dk.setups.celle.util.cell.CellUtils;
import dk.setups.celle.util.NearbyPlayerMap;
import dk.setups.celle.util.PlayerSignDisallow;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.injector.annotation.PostConstruct;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class TaskUpdateCells extends BukkitRunnable {

    private @Inject StoreManager stores;
    private @Inject CellUtils utils;
    private @Inject Plugin plugin;
    private @Inject PlayerSignDisallow disallow;
    private @Inject CellAPI api;

    private final Set<Integer> rentedCellIds = new HashSet<>();

    public TaskUpdateCells() {
    }

    @PostConstruct
    public void initRentedIds() {
        stores.getCellStore().getCache().getAll().stream()
                .filter(Cell::isRented)
                .forEach(cell -> rentedCellIds.add(cell.getId()));
    }

    @Override
    public void run() {
        if(!plugin.isEnabled()) {
            cancel();
            return;
        }
        NearbyPlayerMap nearby = NearbyPlayerMap.from(disallow.filter(Bukkit.getOnlinePlayers()));
        for(Cell cell : stores.getCellStore().getCache().getAll()) {
            updateSet(cell);

            if(cell.getSign() == null) {
                continue;
            }
            utils.updateSign(nearby, cell);
        }
    }

    protected void updateSet(Cell cell) {
        if(!cell.isRented() && rentedCellIds.contains(cell.getId())) {
            rentedCellIds.remove(cell.getId());
            api.expireCell(cell);

            Bukkit.getScheduler().runTaskAsynchronously(plugin, ()
                    -> stores.getCellStore().get(cell.getId()).ifPresent(utils::update));
        }
        if(cell.isRented()) {
            rentedCellIds.add(cell.getId());
        }

    }
}