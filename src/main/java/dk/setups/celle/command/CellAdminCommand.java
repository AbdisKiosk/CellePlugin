package dk.setups.celle.command;

import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dk.setups.celle.cell.*;
import dk.setups.celle.cell.log.CellLogFilter;
import dk.setups.celle.cell.log.CellLogFilterBuilder;
import dk.setups.celle.config.Config;
import dk.setups.celle.config.DefaultConfig;
import dk.setups.celle.config.LangConfig;
import dk.setups.celle.database.StoreManager;
import dk.setups.celle.gui.cell.logs.CellLogsGUI;
import dk.setups.celle.gui.cell.logs.CellLogsGUIState;
import dk.setups.celle.sign.AvailableCellsGUISign;
import dk.setups.celle.sign.CellSign;
import dk.setups.celle.util.WorldEditUtils;
import dk.setups.celle.util.cell.CellFactory;
import dk.setups.celle.util.cell.CellUtils;
import dk.setups.celle.util.WorldGuardUtils;
import eu.okaeri.commands.annotation.*;
import eu.okaeri.commands.bukkit.annotation.Async;
import eu.okaeri.commands.bukkit.annotation.Permission;
import eu.okaeri.commands.bukkit.annotation.Sync;
import eu.okaeri.commands.service.CommandService;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.bukkit.i18n.BI18n;
import eu.okaeri.tasker.core.Tasker;
import eu.okaeri.tasker.core.chain.TaskerChain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Async
@Command(label = "#{commandCeaLabel}", description = "${commandCeaDescription}", aliases = {"#{commandCeaAlias}"})
@Permission("cell.admin")
public class CellAdminCommand implements CommandService {

    private @Inject("storeManager") StoreManager stores;
    private @Inject Tasker tasker;
    private @Inject("lang") BI18n i18n;
    private @Inject Config config;
    private @Inject LangConfig lang;
    private @Inject WorldEditUtils worldEdit;
    private @Inject WorldGuardUtils worldGuard;
    private @Inject CellFactory factory;
    private @Inject Plugin plugin;
    private @Inject CellUtils utils;
    private @Inject CellLogsGUI logsGUI;
    private @Inject DefaultConfig defaults;


    @Executor(pattern = {"#{commandCeaCreateAutoAlias}"}, description = "${commandCeaCreateAutoDescription}", usage = "${commandCeaCreateAutoUsage}")
    public void createAuto(@Context Player executor, @Arg CellGroup group, @Arg String name) {
        Block target = executor.getTargetBlock((Set<Material>) null, 5);
        if (target == null || !target.getType().equals(Material.WALL_SIGN)) {
            i18n.get(lang.getCommandCeaCreateAutoNotLookingAtSign()).sendTo(executor);
            return;
        }

        Optional<CuboidRegion> selection = worldEdit.getSelection(executor);
        if (!selection.isPresent()) {
            i18n.get(lang.getCommandCeaCreateAutoNoSelection())
                    .with("region", name)
                    .sendTo(executor);
            return;
        }
        try {
            createCell(executor, group, worldGuard.create(name, selection.get()), name)
                    .acceptAsync(cell -> {
                        cell.setSign(new CellSign(target.getLocation()));
                        stores.getSignStore().persist(cell.getSign());
                        utils.updateAndSave(cell);
                    })
                    .execute();
        } catch(IllegalArgumentException ex) {
            i18n.get(lang.getCommandCeaCreateAutoRegionAlreadyExists())
                    .with("region", name)
                    .sendTo(executor);
        }
    }
    @Executor(pattern = "#{commandCeaCreateCellAlias}", description = "${commandCeaCreateCellDescription}", usage = "${commandCeaCreateCellUsage}")
    @Async
    public TaskerChain<Cell> createCell(@Context Player executor, @Arg CellGroup group, @Arg ProtectedRegion region, @Arg String name) {
        return tasker.newChain()
                .abortIfAsync(() -> {
                    if(stores.getCellStore().getFromName(name).isPresent()) {
                        i18n.get(lang.getCommandCeaCreateCellAlreadyExists())
                                .with("name", name)
                                .sendTo(executor);
                        return true;
                    }
                    return false;
                })
                .supplyAsync(() -> {
                    CellRegion cellRegion = new CellRegion(region.getId(), executor.getWorld().getName());
                    stores.getRegionStore().persist(cellRegion);

                    Cell cell = new Cell(name, group, cellRegion);
                    stores.getCellStore().persist(cell);

                    i18n.get(lang.getCommandCeaCreateCellCreated()).with("cell", cell).sendTo(executor);

                    return cell;
                });
    }

    @Executor(pattern = "#{commandCeaCellSetSignAlias}", description = "${commandCeaCellSetSignDescription}", usage = "${commandCeaCellSetSignUsage}")
    @Completion(arg = "cell", value = "@cells")
    @Async
    public void setSign(@Context Player player, @Arg Cell cell) {
        Block target = player.getTargetBlock((Set<Material>) null, 5);
        if (target == null || !target.getType().equals(Material.WALL_SIGN)) {
            i18n.get(lang.getCommandCeaCellSetSignNotLookingAtSign()).sendTo(player);
            return;
        }
        cell.setSign(new CellSign(target.getLocation()));
        stores.getSignStore().persist(cell.getSign());
        utils.updateAndSave(cell);
        i18n.get(lang.getCommandCeaCellSetSignSuccess()).with("cell", cell).sendTo(player);
    }


    @Executor(pattern = "#{commandCeaCreateGroupAlias}", description = "${commandCeaCreateGroupDescription}", usage = "${commandCeaCreateGroupUsage}")
    @Async
    public void createGroup(@Context CommandSender executor, @Arg String name) {
         if(stores.getGroupStore().getFromName(name).isPresent()) {
             i18n.get(lang.getCommandCeaCreateGroupAlreadyExists())
                     .with("group", stores.getGroupStore().getFromName(name))
                     .sendTo(executor);
             return;
         }
         CellGroup group = factory.createGroup(name);
         stores.getGroupStore().persist(group);

         i18n.get(lang.getCommandCeaCreateGroupCreated()).with("group", group).sendTo(executor);
    }

    @Executor(pattern = "#{commandCeaDeleteCellAlias}" , description = "${commandCeaDeleteCellDescription}", usage = "${commandCeaDeleteCellUsage}")
    @Async
    public void deleteCell(@Context CommandSender sender, @Arg Cell cell) {
        stores.getCellStore().delete(cell.getId());
        worldGuard.delete(cell.getRegion());
        i18n.get(lang.getCommandCeaDeleteCellSuccess()).with("cell", cell).sendTo(sender);
    }

    @Executor(pattern = "#{commandCeaUnrentAlias}", description = "${commandCeaUnrentDescription}", usage = "${commandCeaUnrentUsage}")
    @Async
    public void unrentCell(@Context CommandSender sender, @Arg Cell cell) {
        cell.unrent();
        utils.updateAndSave(cell);
        i18n.get(lang.getCommandCeaUnrentSuccess()).with("cell", cell).sendTo(sender);
    }

    @Executor(pattern = "#{commandCeaUnrentAllAlias}", description = "${commandCeaUnrentAllDescription}", usage = "${commandCeaUnrentAllUsage}")
    @Async
    public void unrentAllCells(@Context CommandSender sender, @Arg CellUser user) {
        Collection<Cell> cells = stores.getCellStore().getOwnedCells(user);
        for(Cell cell : cells) {
            cell.unrent();
            utils.updateAndSave(cell);
        }

        i18n.get(lang.getCommandCeaUnrentAllSuccess())
                .with("count", cells.size())
                .with("player", user.getName())
                .sendTo(sender);
    }

    @Executor(pattern = "#{commandCeaExtendCellAlias}", description = "${commandCeaExtendCellDescription}", usage = "${commandCeaExtendCellUsage}")
    @Async
    public void extendCell(@Context CommandSender sender, @Arg Cell cell) {
        if(!cell.isRented()) {
            i18n.get(lang.getCommandCeaExtendCellNotRented())
                    .with("cell", cell)
                    .sendTo(sender);
            return;
        }
        cell.extend();
        utils.updateAndSave(cell);
        i18n.get(lang.getCommandCeaExtendCellSuccess())
                .with("cell", cell)
                .sendTo(sender);
    }

    @Executor(pattern = "#{commandCeaDeleteGroupAlias}", description = "${commandCeaDeleteGroupDescription}", usage = "${commandCeaDeleteGroupUsage}")
    @Async
    public void deleteGroup(@Context CommandSender sender, @Arg CellGroup group, @Arg CellGroup newGroup) throws Exception {
        Set<Cell> cellsInGroup = new HashSet<>(group.getCells());
        i18n.get(lang.getCommandCeaDeleteGroupFoundCellsInGroup())
                .with("count", cellsInGroup.size())
                .sendTo(sender);

        stores.getCellStore().getDao().callBatchTasks(() -> {
            cellsInGroup.forEach(cell -> {
                cell.setGroup(newGroup);
                stores.getCellStore().persist(cell);
            });
            return null;
        });

        stores.getGroupStore().delete(group.getId());
        i18n.get(lang.getCommandCeaDeleteCellSuccess())
                .with("name", group.getName())
                .sendTo(sender);

        i18n.get(lang.getCommandCeaDeleteGroupSuccess())
                .with("group", group)
                .with("newGroup", newGroup)
                .with("count", cellsInGroup.size())
                .sendTo(sender);
    }

    @Executor(pattern = "#{commandCeaLogsPlayerAlias}", description = "${commandCeaLogsPlayerDescription}", usage = "${commandCeaLogsPlayerUsage}")
    @Async
    public void logsPlayer(@Context Player player, @Arg CellUser user) {
        CellLogFilter filter = new CellLogFilterBuilder()
                .user(user)
                .targetOrUser(true)
                .build();
        logsGUI.create(new CellLogsGUIState(player, filter)).open(player);
    }

    @Executor(pattern = "#{commandCeaLogsCellAlias}", description = "${commandCeaLogsCellDescription}", usage = "${commandCeaLogsCellUsage}")
    @Async
    public void logsCell(@Context Player player, @Arg Cell cell) {
        CellLogFilter filter = new CellLogFilterBuilder()
                .cell(cell)
                .build();
        logsGUI.create(new CellLogsGUIState(player,filter)).open(player);
    }

    @Executor(pattern = "#{commandCeaGroupSetSignLineAlias}", description = "${commandCeaGroupSetSignLineDescription}", usage = "${commandCeaGroupSetSignLineUsage}")
    @Completion(arg = "group", value = "@groups")
    @Completion(arg = "state", value = {"unrented", "rented-non-member", "rented-member", "rented-owner"})
    @Completion(arg = "line", value = {"1", "2", "3", "4"})
    @Async
    public void setSignLine(@Context Player player, @Arg CellGroup group, @Arg String state, @Arg int line, @Arg String text) {
        if(line < 1 || line > 4) {
            i18n.get(lang.getCommandCeaGroupSetSignLineNotCorrectLine()).with("line", line).sendTo(player);
            return;
        }
        switch(state.toLowerCase()) {
            case "unrented":
                group.setUnrentedSignLine(line, text);
                break;
            case "rented-non-member":
                group.setRentedNonMemberSignLine(line, text);
                break;
            case "rented-member":
                group.setRentedMemberSignLine(line, text);
                break;
            case "rented-owner":
                group.setRentedOwnerSignLine(line, text);
                break;
            default:
                i18n.get(lang.getCommandCeaGroupSetSignLineNotCorrectState()).with("state", state).sendTo(player);
                return;
        }
        i18n.get(lang.getCommandCeaGroupSetSignLineSuccess())
                .with("group", group)
                .with("state", state)
                .with("line", line)
                .with("text", text).sendTo(player);
        stores.getGroupStore().persist(group);
        stores.getCellStore().updateCache();
    }


    @Executor(pattern = "#{commandCeaGroupSetMaxRentTimeAlias}", description = "${commandCeaGroupSetMaxRentTimeDescription}", usage = "${commandCeaGroupSetMaxRentTimeUsage}")
    @Completion(arg = "group", value = "@groups")
    @Async
    public void setGroupMaxRentTime(@Context Player player, @Arg CellGroup group, @Arg Duration time) {
        group.setMaxRentTimeMillis(time.toMillis());
        stores.getGroupStore().persist(group);
        i18n.get(lang.getCommandCeaGroupSetMaxRentTimeSuccess())
                .with("group", group)
                .with("time", time).sendTo(player);
    }

    @Executor(pattern = "#{commandCeaGroupSetRentTimeAlias}", description = "${commandCeaGroupSetRentTimeDescription}", usage = "${commandCeaGroupSetRentTimeUsage}")
    @Completion(arg = "group", value = "@groups")
    @Async
    public void setGroupRentTime(@Context Player player, @Arg CellGroup group, @Arg Duration time) {
        group.setRentTimeMillis(time.toMillis());
        stores.getGroupStore().persist(group);
        i18n.get(lang.getCommandCeaGroupSetRentTimeSuccess())
                .with("group", group)
                .with("time", time).sendTo(player);
    }

    @Executor(pattern = "#{commandCeaGroupSetMaxRentedCellsAlias}", description = "${commandCeaGroupSetMaxRentedCellsDescription}", usage = "${commandCeaGroupSetMaxRentedCellsUsage}")
    @Completion(arg = "group", value = "@groups")
    @Async
    public void setGroupMaxRentedCells(@Context Player player, @Arg CellGroup group, @Arg int count) {
        group.setMaxRentedCells(count);
        stores.getGroupStore().persist(group);
        i18n.get(lang.getCommandCeaGroupSetMaxRentedCellsSuccess())
                .with("group", group)
                .with("count", count).sendTo(player);
    }

    @Executor(pattern = "#{commandCeaGroupInfoAlias}", description = "${commandCeaGroupInfoDescription}", usage = "${commandCeaGroupInfoUsage}")
    @Completion(arg = "group", value = "@groups")
    @Async
    public void groupInfo(@Context Player player, @Arg CellGroup group) {
        i18n.get(lang.getCommandCeaGroupInfoMessage())
                .with("group", group)
                .sendTo(player);
    }

    @Executor(pattern = "#{commandCeaSignGUICreateAlias}", description = "${commandCeaSignGUICreateDescription}", usage = "${commandCeaSignGUICreateUsage}")
    @Async
    public void createGUISign(@Context Player player, @Arg String region) {
        Block target = player.getTargetBlock((Set<Material>) null, 5);
        if(target == null || !target.getType().equals(Material.WALL_SIGN)) {
            i18n.get(lang.getCommandCeaSignGuiCreateNotLookingAtSign()).sendTo(player);
            return;
        }
        Location location = target.getLocation();
        if(stores.getAvailableCellsGuiSignStore().getSign(target.getLocation()).isPresent()) {
            i18n.get(lang.getCommandCeaSignGUICreateAlias()).sendTo(player);
            return;
        }
        if(!worldGuard.getRegionByName(location.getWorld(), region).isPresent()) {
            i18n.get(lang.getCommandCeaSignGuiCreateRegionNotFound())
                    .with("region", region)
                    .sendTo(player);
            return;
        }
        AvailableCellsGUISign sign =
                new AvailableCellsGUISign(location.getBlockX(), location.getBlockY(), location.getBlockZ(),
                        region, location.getWorld().getName());
        stores.getAvailableCellsGuiSignStore().persist(sign);
        i18n.get(lang.getCommandCeaSignGuiCreateSuccess()).sendTo(player);
    }

    @Executor(pattern = "#{commandCeaSignGUIDeleteAlias}", description = "${commandCeaSignGUIDeleteDescription}", usage = "${commandCeaSignGUIDeleteUsage}")
    @Async
    public void deleteGUISign(@Context Player player) {
        Block target = player.getTargetBlock((Set<Material>) null, 5);
        if(target == null || !target.getType().equals(Material.WALL_SIGN)
            || !stores.getAvailableCellsGuiSignStore().getSign(target.getLocation()).isPresent()) {
            i18n.get(lang.getCommandCeaSignGuiDeleteSignNotFound()).sendTo(player);
            return;
        }
        stores.getAvailableCellsGuiSignStore().getSign(target.getLocation()).ifPresent(sign -> {
            stores.getAvailableCellsGuiSignStore().delete(sign.getId());
            i18n.get(lang.getCommandCeaSignGuiDeleteSuccess()).sendTo(player);
        });
    }

    @Executor(pattern = "#{commandCeaReloadAlias}", description = "${commandCeaReloadDescription}", usage = "${commandCeaReloadUsage}")
    @Sync
    public void reload(@Context CommandSender sender) {
        long started = System.currentTimeMillis();

        config.load();
        defaults.load();

        long elapsed = System.currentTimeMillis() - started;
        i18n.get(lang.getCommandCeaReloadSuccess())
                .with("elapsed", elapsed)
                .sendTo(sender);
    }
}
