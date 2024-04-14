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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskUpdateCells extends BukkitRunnable {

    private @Inject StoreManager stores;
    private @Inject CellUtils utils;
    private @Inject Plugin plugin;
    private @Inject PlayerSignDisallow disallow;
    private @Inject CellAPI api;

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

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
            executor.submit(() -> updateSet(cell));

            if(cell.getSign() == null) {
                continue;
            }
            utils.updateSign(nearby, cell);
        }
    }

    protected void updateSet(Cell cached) {
        if(!cached.isRented() && rentedCellIds.contains(cached.getId())) {
            Cell cell = stores.getCellStore().get(cached.getId()).orElseThrow(() -> new IllegalStateException("Cell not found"));

            rentedCellIds.remove(cell.getId());
            api.expireCell(cell);

            Bukkit.getScheduler().runTaskAsynchronously(plugin, ()
                    -> stores.getCellStore().get(cell.getId()).ifPresent(utils::update));
        }
        if(cached.isRented()) {
            rentedCellIds.add(cached.getId());
        }

    }
}