package dk.setups.celle.listener;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dk.setups.celle.cell.Cell;
import dk.setups.celle.command.CellCommand;
import dk.setups.celle.database.StoreManager;
import dk.setups.celle.gui.cell.CellAdminGUI;
import dk.setups.celle.gui.cell.CellGUIState;
import dk.setups.celle.gui.region.CellsInRegionGUI;
import dk.setups.celle.gui.region.CellsInRegionGUIState;
import dk.setups.celle.sign.AvailableCellsGUISign;
import dk.setups.celle.util.WorldGuardUtils;
import dk.setups.celle.util.cell.CellRentManager;
import dk.setups.celle.util.CooldownUtil;
import dk.setups.celle.util.SignContentCreator;
import eu.okaeri.commands.service.Option;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;

@Component
public class SignClickListener implements Listener {

    private @Inject CellRentManager cellRent;
    private @Inject StoreManager stores;
    private @Inject CooldownUtil cooldown;
    private @Inject SignContentCreator signContentCreator;
    private @Inject CellCommand cellCommand;
    private @Inject Plugin plugin;
    private @Inject CellAdminGUI gui;
    private @Inject CellsInRegionGUI availableCellsGui;
    private @Inject WorldGuardUtils worldGuard;

    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if(!isSign(block)) {
            return;
        }
        if(cooldown.checkCooldown(player)) {
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Optional<Cell> cell = stores.getCellStore().getFromSignLoc(
                    block.getX(), block.getY(), block.getZ(), block.getWorld().getName());

            if(!cell.isPresent()) {
                stores.getAvailableCellsGuiSignStore().getSign(block.getLocation()).ifPresent(sign -> {
                    availableCellsGui.create(new CellsInRegionGUIState(player, sign.getRegion(worldGuard))).open(player);
                });
                return;
            }

            if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                handleRightClickAsync(player, cell.get());
            } else if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                handleLeftClickAsync(player, block, cell.get());
            }
        });
    }
    private void handleRightClickAsync(Player player, Cell cell) {
        cellRent.handleCellUse(player, cell);
    }

    private void handleLeftClickAsync(Player player, Block block, Cell cell) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            player.sendSignChange(block.getLocation(), signContentCreator.getSignContent(cell, player));
            cellCommand.info(player, Option.of(cell));
        });
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if(!isSign(block)) {
            return;
        }
        if(!event.getPlayer().isOp()) {
            return;
        }
        AvailableCellsGUISign sign = stores.getAvailableCellsGuiSignStore().getSign(block.getLocation()).orElse(null);
        if(sign != null) {
            event.setCancelled(true);
            return;
        }
        Cell cell = stores.getCellStore().getFromSignLoc(
                block.getX(), block.getY(), block.getZ(), block.getWorld().getName()).orElse(null);
        if(cell == null) {
            return;
        }
        if(!event.getPlayer().isSneaking()) {
            event.setCancelled(true);
            gui.create(new CellGUIState(event.getPlayer(), cell)).open(event.getPlayer());
            return;
        }

        stores.getSignStore().delete(cell.getSign().getId());
        cell.setSign(null);
        stores.getCellStore().persist(cell);
    }

    private boolean isSign(Block block) {
        if(block == null) {
            return false;
        }
        return block.getType().equals(Material.WALL_SIGN);
    }
}